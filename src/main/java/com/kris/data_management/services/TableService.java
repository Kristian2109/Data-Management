package com.kris.data_management.services;

import com.kris.data_management.common.ColumnDataType;
import com.kris.data_management.common.CreateColumnDto;
import com.kris.data_management.common.CreateTableDto;
import com.kris.data_management.logical.query.Query;
import com.kris.data_management.logical.repository.TableMetadataRepository;
import com.kris.data_management.logical.table.ColumnMetadata;
import com.kris.data_management.logical.table.CreateColumnMetadataDto;
import com.kris.data_management.logical.table.CreateTableMetadataDto;
import com.kris.data_management.logical.table.TableMetadata;
import com.kris.data_management.physical.dto.CreatePhysicalTableResult;
import com.kris.data_management.physical.exception.ResourceNotFoundException;
import com.kris.data_management.physical.query.PhysicalQuery;
import com.kris.data_management.physical.query.QueryResult;
import com.kris.data_management.physical.repository.PhysicalTableRepository;
import com.kris.data_management.physical.repository.PhysicalTableRepositoryImpl;
import com.kris.data_management.utils.TableMetadataMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class TableService {
    private final PhysicalTableRepository physicalTableRepository;
    private final TableMetadataRepository tableMetadataRepository;

    public TableService(PhysicalTableRepositoryImpl tableRepository, TableMetadataRepository tableMetadataRepository) {
        this.physicalTableRepository = tableRepository;
        this.tableMetadataRepository = tableMetadataRepository;
    }

    @Transactional
    public TableMetadata createTable(CreateTableDto tableDto) {
        CreatePhysicalTableResult res = physicalTableRepository.createTable(tableDto);
        Map<String, ColumnDataType> dataTypeByDisplayName = tableDto.columns().stream()
            .collect(Collectors.toMap(
                CreateColumnDto::displayName,
                CreateColumnDto::type,
                (existing, replacement) -> existing
            ));

        List<CreateColumnMetadataDto> columns = res.columnsByDisplayName()
            .entrySet()
            .stream()
            .map(c ->{
                if (c.getValue().equals("id")) {
                    return new CreateColumnMetadataDto("Id", "id", ColumnDataType.NUMBER);
                }
                ColumnDataType columnDataType = dataTypeByDisplayName.get(c.getKey());

                return new CreateColumnMetadataDto(c.getKey(), c.getValue(), columnDataType);
            })
            .toList();

        CreateTableMetadataDto tableMetadataCreate = new CreateTableMetadataDto(tableDto.displayName(),
                res.tableName(), columns);

        return this.tableMetadataRepository.createTable(tableMetadataCreate);
    }

    @Transactional
    public ColumnMetadata createColumn(Long tableId, CreateColumnDto columnDto) {
        TableMetadata table = tableMetadataRepository.getTable(tableId);
        String physicalColumnName = physicalTableRepository.addColumn(table.getPhysicalName(), columnDto);
        CreateColumnMetadataDto columnMetadataDto = TableService.mapToColumnMetadata(columnDto, physicalColumnName);

        TableMetadata updatedTable = tableMetadataRepository.addColumn(tableId, columnMetadataDto);

        ColumnMetadata columnMetadata = updatedTable.getColumns().stream()
                .filter(c -> c.getPhysicalName().equals(columnMetadataDto.physicalName()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(
                        "Column metadata for table " + updatedTable.getDisplayName() + " was not added properly"));

        return columnMetadata;
    }

    @Transactional(readOnly = true)
    public List<TableMetadata> getTablesForDatabase() {
        return tableMetadataRepository.getAllTables();
    }

    @Transactional(readOnly = true)
    public TableMetadata getById(Long id) {
        return tableMetadataRepository.getTable(id);
    }

    @Transactional
    public void addRecord(Long tableId, Map<Long, String> valuePerColumn) {
        TableMetadata table = tableMetadataRepository.getTable(tableId);
        Map<String, String> valuePerPhysicalColumn = new HashMap<>();
        for (Map.Entry<Long, String> entry: valuePerColumn.entrySet()) {
            ColumnMetadata column = table.getColumns().stream()
                .filter(c -> Objects.equals(c.getId(), entry.getKey()))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Column Metadata", entry.getKey()));

            valuePerPhysicalColumn.put(column.getPhysicalName(), entry.getValue());

        }
        physicalTableRepository.addRecord(table.getPhysicalName(), valuePerPhysicalColumn);
    }

    @Transactional
    public void addRecordsBatch(Long tableId, List<Long> columnIds, List<List<String>> records) {
        TableMetadata table = tableMetadataRepository.getTable(tableId);

        List<String> columnNames = table.getColumns().stream()
            .filter(metadata -> columnIds.contains(metadata.getId()))
            .map(ColumnMetadata::getPhysicalName)
            .toList();

        physicalTableRepository.addRecords(table.getPhysicalName(), columnNames, records);
    }

    @Transactional(readOnly = true)
    public QueryResult queryRecords(Long tableId, Query query) {
        TableMetadata table = tableMetadataRepository.getTable(tableId);

        PhysicalQuery physicalQuery = TableMetadataMapper.mapToPhysicalQuery(query, List.of(table));

        return physicalTableRepository.executeQuery(physicalQuery);
    }

    private static CreateColumnMetadataDto mapToColumnMetadata(CreateColumnDto c,
            String physicalColumn) {
        return new CreateColumnMetadataDto(c.displayName(), physicalColumn, c.type());
    }
}