package com.kris.data_management.logical.repository;

import java.util.List;

import com.kris.data_management.logical.table.BaseTableMetadata;
import com.kris.data_management.logical.table.CreateTableViewDto;
import com.kris.data_management.logical.table.CreateColumnMetadataDto;
import com.kris.data_management.logical.table.CreateTableMetadataDto;
import com.kris.data_management.logical.table.FullTableMetadata;

public interface TableMetadataRepository {
    FullTableMetadata createTable(CreateTableMetadataDto tableDto);
    FullTableMetadata addColumn(String tablePhysicalName, CreateColumnMetadataDto columnDto);
    FullTableMetadata getTable(String tablePhysicalName);
    List<BaseTableMetadata> getAllTables();
    FullTableMetadata save(FullTableMetadata table);

    FullTableMetadata addView(String tablePhysicalName, CreateTableViewDto viewDto);
    void deleteTable(Long tableId);
    void deleteColumn(Long tableId, Long columnId);
}
