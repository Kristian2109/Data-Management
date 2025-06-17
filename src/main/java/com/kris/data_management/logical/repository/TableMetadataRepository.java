package com.kris.data_management.logical.repository;

import java.util.List;

import com.kris.data_management.common.CreateColumnDto;
import com.kris.data_management.logical.table.ColumnMetadata;
import com.kris.data_management.logical.table.CreateColumnMetadataDto;
import com.kris.data_management.logical.table.CreateTableMetadataDto;
import com.kris.data_management.logical.table.TableMetadata;

public interface TableMetadataRepository {
    TableMetadata createTable(CreateTableMetadataDto tableDto);
    TableMetadata addColumn(Long tableId, CreateColumnMetadataDto columnDto);
    TableMetadata getTable(Long tableId);
    List<TableMetadata> getAllTables();
    void deleteTable(Long tableId);
    void deleteColumn(Long tableId, Long columnId);
}
