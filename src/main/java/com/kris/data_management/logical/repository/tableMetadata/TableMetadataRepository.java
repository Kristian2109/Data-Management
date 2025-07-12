package com.kris.data_management.logical.repository.tableMetadata;

import java.util.List;

import com.kris.data_management.logical.table.BaseTableMetadata;
import com.kris.data_management.logical.table.CreateTableViewDto;
import com.kris.data_management.logical.table.CreateColumnMetadataDto;
import com.kris.data_management.logical.table.CreateTableMetadataDto;
import com.kris.data_management.logical.table.FullTableMetadata;
import com.kris.data_management.logical.table.UpdateColumnDto;
import com.kris.data_management.logical.table.UpdateTableDto;

public interface TableMetadataRepository {
    FullTableMetadata createTable(CreateTableMetadataDto tableDto);
    FullTableMetadata addColumn(String tablePhysicalName, CreateColumnMetadataDto columnDto);
    FullTableMetadata getTable(String tablePhysicalName);
    List<BaseTableMetadata> getAllTables();
    FullTableMetadata save(FullTableMetadata table);

    void updateTable(String tableId, UpdateTableDto dto);
    FullTableMetadata addView(String tablePhysicalName, CreateTableViewDto viewDto);
    void updateColumn(String tableName, String columnName, UpdateColumnDto dto);
    void deleteTable(String tableId);
    void deleteColumn(String tableId, String columnId);
}
