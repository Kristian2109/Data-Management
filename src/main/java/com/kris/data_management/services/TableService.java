package com.kris.data_management.services;

import com.kris.data_management.common.ColumnTypeMapper;
import com.kris.data_management.common.CreateColumnDto;
import com.kris.data_management.common.CreateTableDto;
import com.kris.data_management.database.DatabaseContext;
import com.kris.data_management.logical.repository.TableMetadataRepository;
import com.kris.data_management.logical.table.ColumnMetadata;
import com.kris.data_management.logical.table.CreateColumnMetadataDto;
import com.kris.data_management.logical.table.CreateTableMetadataDto;
import com.kris.data_management.logical.table.TableMetadata;
import com.kris.data_management.physical.dto.CreatePhysicalColumnDto;
import com.kris.data_management.physical.dto.CreatePhysicalTableDto;
import com.kris.data_management.physical.repository.PhysicalTableRepository;
import com.kris.data_management.physical.repository.PhysicalTableRepositoryImpl;
import com.kris.data_management.utils.StorageUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TableService {
    private static final String TABLE_PREFIX = "tb";
    private static final String COLUMN_PREFIX = "col";
    private static final Integer RANDOM_PART_SIZE = 5;

    private final PhysicalTableRepository physicalTableRepository;
    private final TableMetadataRepository tableMetadataRepository;

    public TableService(PhysicalTableRepositoryImpl tableRepository, TableMetadataRepository tableMetadataRepository) {
        this.physicalTableRepository = tableRepository;
        this.tableMetadataRepository = tableMetadataRepository;
    }

    @Transactional
    public TableMetadata createTable(CreateTableDto tableDto) {
        List<CreateColumnMetadataDto> columns = tableDto.columns().stream()
            .map(TableService::mapToColumnMetadata)
            .toList();
        String physicalTableName = createUniqueTableName(tableDto.displayName());
        CreateTableMetadataDto tableMetadataCreate =
            new CreateTableMetadataDto(tableDto.displayName(), physicalTableName, columns);

        TableMetadata tableMetadata = this.tableMetadataRepository.createTable(tableMetadataCreate);

        List<CreatePhysicalColumnDto> physicalColumnDto = tableMetadata.getColumns().stream()
            .map(TableService::mapToPhysicalColumn)
            .toList();
        CreatePhysicalTableDto createPhysicalTableDto = new CreatePhysicalTableDto(physicalTableName, physicalColumnDto);

        physicalTableRepository.createTable(createPhysicalTableDto);

        return tableMetadata;
    }

    @Transactional
    public ColumnMetadata createColumn(Long tableId, CreateColumnDto columnDto) {
        CreateColumnMetadataDto columnMetadataDto = TableService.mapToColumnMetadata(columnDto);
        TableMetadata table = tableMetadataRepository.addColumn(tableId, columnMetadataDto);

        ColumnMetadata columnMetadata = table.getColumns().stream()
            .filter(c -> c.getPhysicalName().equals(columnMetadataDto.physicalName()))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Column metadata for table " + table.getDisplayName() + " was not added properly"));

        physicalTableRepository.addColumn(table.getPhysicalName(), TableService.mapToPhysicalColumn(columnMetadata));

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

    private static String createUniqueTableName(String displayName) {
        return StorageUtils.createPhysicalName(TABLE_PREFIX, displayName, RANDOM_PART_SIZE);
    }

    private static String createUniqueColumnName(String displayName) {
        return StorageUtils.createPhysicalName(COLUMN_PREFIX, displayName, RANDOM_PART_SIZE);
    }

    private static CreateColumnMetadataDto mapToColumnMetadata(CreateColumnDto c) {
        return new CreateColumnMetadataDto(c.displayName(), createUniqueColumnName(c.displayName()), c.type());
    }

    private static CreatePhysicalColumnDto mapToPhysicalColumn(ColumnMetadata c) {
        return new CreatePhysicalColumnDto(ColumnTypeMapper.map(c.getType()), c.getPhysicalName());
    }

} 