package com.kris.data_management.services;

import com.kris.data_management.common.ColumnDataType;
import com.kris.data_management.common.CreateColumnDto;
import com.kris.data_management.common.CreateRecordDto;
import com.kris.data_management.common.CreateTableDto;
import com.kris.data_management.common.exception.ResourceNotFoundException;
import com.kris.data_management.logical.repository.TableMetadataRepository;
import com.kris.data_management.logical.table.ColumnMetadata;
import com.kris.data_management.logical.table.CreateColumnMetadataDto;
import com.kris.data_management.logical.table.CreateTableMetadataDto;
import com.kris.data_management.logical.table.TableMetadata;
import com.kris.data_management.physical.dto.CreatePhysicalTableResult;
import com.kris.data_management.physical.query.PhysicalQuery;
import com.kris.data_management.physical.query.QueryResult;
import com.kris.data_management.physical.repository.PhysicalTableRepository;
import com.kris.data_management.physical.repository.PhysicalTableRepositoryImpl;
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
                        (existing, replacement) -> existing));

        List<CreateColumnMetadataDto> columns = res.columnsByDisplayName()
                .entrySet()
                .stream()
                .map(c -> {
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
    public ColumnMetadata createColumn(String physicalTableName, CreateColumnDto columnDto) {
        TableMetadata table = tableMetadataRepository.getTable(physicalTableName);
        String physicalColumnName = physicalTableRepository.addColumn(table.getPhysicalName(), columnDto);
        CreateColumnMetadataDto columnMetadataDto = TableService.mapToColumnMetadata(columnDto, physicalColumnName);

        TableMetadata updatedTable = tableMetadataRepository.addColumn(physicalTableName, columnMetadataDto);

        return updatedTable.getColumnByName(physicalColumnName);
    }

    @Transactional(readOnly = true)
    public List<TableMetadata> getTablesForDatabase() {
        return tableMetadataRepository.getAllTables();
    }

    @Transactional(readOnly = true)
    public TableMetadata getById(String name) {
        return tableMetadataRepository.getTable(name);
    }

    @Transactional
    public void addRecord(String tableName, CreateRecordDto recordDto) {
        physicalTableRepository.addRecord(tableName, recordDto);
    }

    @Transactional
    public void addRecordsBatch(String tableName, List<String> columnNames, List<List<String>> records) {
        physicalTableRepository.addRecords(tableName, columnNames, records);
    }

    @Transactional(readOnly = true)
    public QueryResult queryRecords(String tableName, PhysicalQuery query) {
        return physicalTableRepository.executeQuery(tableName, query);
    }

    private static CreateColumnMetadataDto mapToColumnMetadata(CreateColumnDto c,
            String physicalColumn) {
        return new CreateColumnMetadataDto(c.displayName(), physicalColumn, c.type());
    }
}