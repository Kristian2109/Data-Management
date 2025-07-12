package com.kris.data_management.services;

import com.kris.data_management.logical.table.*;
import com.kris.data_management.physical.dto.query.Pagination;
import com.kris.data_management.physical.dto.table.ColumnDataType;
import com.kris.data_management.physical.dto.table.CreateColumnDto;
import com.kris.data_management.physical.dto.record.UpdateRecordDto;
import com.kris.data_management.physical.dto.table.CreateTableDto;
import com.kris.data_management.logical.repository.tableMetadata.TableMetadataRepository;
import com.kris.data_management.physical.dto.table.CreatePhysicalTableResult;
import com.kris.data_management.physical.dto.query.PhysicalQuery;
import com.kris.data_management.physical.dto.query.QueryResult;
import com.kris.data_management.physical.repository.PhysicalTableRepository;
import com.kris.data_management.physical.repository.PhysicalTableRepositoryImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TableService {
    private final PhysicalTableRepository physicalTableRepository;
    private final TableMetadataRepository tableMetadataRepository;

    public TableService(PhysicalTableRepositoryImpl tableRepository,
                        TableMetadataRepository tableMetadataRepository) {
        this.physicalTableRepository = tableRepository;
        this.tableMetadataRepository = tableMetadataRepository;
    }

    @Transactional
    public FullTableMetadata createTable(CreateTableDto tableDto) {
        CreatePhysicalTableResult res = physicalTableRepository.createTable(tableDto);
        Map<String, CreateColumnDto> dataTypeByDisplayName = tableDto.columns().stream()
                .collect(Collectors.toMap(
                        CreateColumnDto::displayName,
                        (c) -> c,
                        (existing, replacement) -> existing));

        List<CreateColumnMetadataDto> columns = res.columnsByDisplayName()
                .entrySet()
                .stream()
                .map(c -> {
                    if (c.getValue().equals("id")) {
                        return new CreateColumnMetadataDto("Id", "id", ColumnDataType.NUMBER);
                    }

                    CreateColumnDto column = dataTypeByDisplayName.get(c.getKey());
                    return mapToColumnMetadata(column, c.getValue());
                })
                .toList();

        CreateTableMetadataDto tableMetadataCreate = new CreateTableMetadataDto(tableDto.displayName(),
                res.tableName(), columns);

        return this.tableMetadataRepository.createTable(tableMetadataCreate);
    }

    @Transactional
    public ColumnMetadata createColumn(String physicalTableName, CreateColumnDto columnDto) {
        FullTableMetadata table = tableMetadataRepository.getTable(physicalTableName);
        String physicalColumnName = physicalTableRepository.addColumn(table.getPhysicalName(), columnDto);
        CreateColumnMetadataDto columnMetadataDto = TableService.mapToColumnMetadata(columnDto, physicalColumnName);

        FullTableMetadata updatedTable = tableMetadataRepository.addColumn(physicalTableName, columnMetadataDto);

        return updatedTable.getColumnByName(physicalColumnName);
    }

    @Transactional(readOnly = true)
    public List<BaseTableMetadata> getTablesForDatabase() {
        return tableMetadataRepository.getAllTables();
    }

    @Transactional(readOnly = true)
    public FullTableMetadata getById(String name) {
        return tableMetadataRepository.getTable(name);
    }

    @Transactional
    public void addRecord(String tableName, UpdateRecordDto recordDto) {
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

    @Transactional
    public ViewMetadata createView(String tableName, CreateTableViewDto viewDto) {
        PhysicalQuery queryForValidation = new PhysicalQuery(viewDto.query(), new Pagination(0L, 1L));
        physicalTableRepository.executeQuery(tableName, queryForValidation);

        FullTableMetadata table = tableMetadataRepository.addView(tableName, viewDto);
        return table.getViewByName(viewDto.name());
    }

    @Transactional
    public void updateRecord(String tableName, Long recordId, UpdateRecordDto record) {
        physicalTableRepository.updateRecord(tableName, recordId, record);
    }

    @Transactional
    public void deleteRecord(String tableName, Long recordId) {
        physicalTableRepository.deleteRecord(tableName, recordId);
    }

    @Transactional
    public void updateColumn(String tableName, String columnName, UpdateColumnDto dto) {
        tableMetadataRepository.updateColumn(tableName, columnName, dto);
    }

    private static CreateColumnMetadataDto mapToColumnMetadata(CreateColumnDto c,
            String physicalColumn) {
        return new CreateColumnMetadataDto(c.displayName(), physicalColumn, c.type());
    }
}