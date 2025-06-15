package com.kris.data_management.repositories;

import com.kris.data_management.dtos.model.table.ColumnMetadata;
import com.kris.data_management.dtos.model.table.TableMetadata;

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
