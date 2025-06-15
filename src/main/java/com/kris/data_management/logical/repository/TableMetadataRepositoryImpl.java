package com.kris.data_management.logical.repository;

import com.kris.data_management.logical.table.ColumnMetadata;
import com.kris.data_management.logical.table.TableMetadata;

import java.util.List;

public class TableMetadataRepositoryImpl implements TableMetadataRepository {
    @Override
    public TableMetadata createTable(String displayName, String physicalName, List<String> columnNames) {
        return null;
    }

    @Override
    public ColumnMetadata addColumn(Long tableId, String columnName) {
        return null;
    }

    @Override
    public TableMetadata getTable(Long tableId) {
        return null;
    }

    @Override
    public List<TableMetadata> getAllTables() {
        return null;
    }

    @Override
    public void deleteTable(Long tableId) {

    }

    @Override
    public void deleteColumn(Long tableId, Long columnId) {

    }
}
