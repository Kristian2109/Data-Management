package com.kris.data_management.logical.repository;

import java.util.List;

import com.kris.data_management.logical.table.ColumnMetadata;
import com.kris.data_management.logical.table.TableMetadata;

public interface TableMetadataRepository {
    TableMetadata createTable(String displayName, String physicalName, List<String> columnNames);
    ColumnMetadata addColumn(Long tableId, String columnName);
    TableMetadata getTable(Long tableId);
    List<TableMetadata> getAllTables();
    void deleteTable(Long tableId);
    void deleteColumn(Long tableId, Long columnId);
}
