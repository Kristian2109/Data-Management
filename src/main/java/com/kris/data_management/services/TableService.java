package com.kris.data_management.services;

import com.kris.data_management.common.ColumnTypeMapper;
import com.kris.data_management.common.CreateTableDto;
import com.kris.data_management.logical.repository.TableMetadataRepository;
import com.kris.data_management.logical.table.CreateColumnMetadataDto;
import com.kris.data_management.logical.table.CreateTableMetadataDto;
import com.kris.data_management.logical.table.TableMetadata;
import com.kris.data_management.physical.dto.CreatePhysicalColumnDto;
import com.kris.data_management.physical.dto.CreatePhysicalTableDto;
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

    private final PhysicalTableRepositoryImpl physicalTableRepository;
    private final TableMetadataRepository tableMetadataRepository;

    public TableService(PhysicalTableRepositoryImpl tableRepository, TableMetadataRepository tableMetadataRepository) {
        this.physicalTableRepository = tableRepository;
        this.tableMetadataRepository = tableMetadataRepository;
    }

    @Transactional
    public TableMetadata createTable(CreateTableDto tableDto) {
        List<CreateColumnMetadataDto> columns = tableDto.columns().stream()
            .map(c ->
                new CreateColumnMetadataDto(c.displayName(), createUniqueColumnName(c.displayName()), c.type())
            )
            .toList();
        String physicalTableName = createUniqueTableName(tableDto.displayName());
        CreateTableMetadataDto tableMetadataCreate =
            new CreateTableMetadataDto(tableDto.displayName(), physicalTableName, columns);

        TableMetadata tableMetadata = this.tableMetadataRepository.createTable(tableMetadataCreate);

        List<CreatePhysicalColumnDto> physicalColumnDto = tableMetadata.getColumns().stream()
            .map(c -> new CreatePhysicalColumnDto(ColumnTypeMapper.map(c.getType()), c.getPhysicalName()))
            .toList();
        CreatePhysicalTableDto createPhysicalTableDto = new CreatePhysicalTableDto(physicalTableName, physicalColumnDto);

        physicalTableRepository.createTable(createPhysicalTableDto);

        return tableMetadata;
    }

    public String createUniqueTableName(String displayName) {
        return StorageUtils.createPhysicalName(TABLE_PREFIX, displayName, RANDOM_PART_SIZE);
    }

    public String createUniqueColumnName(String displayName) {
        return StorageUtils.createPhysicalName(COLUMN_PREFIX, displayName, RANDOM_PART_SIZE);
    }
} 