package com.kris.data_management.logical.repository;

import java.util.List;

import com.kris.data_management.logical.table.CreateColumnMetadataDto;
import com.kris.data_management.logical.table.CreateTableMetadataDto;
import com.kris.data_management.logical.table.TableMetadata;

public interface TableMetadataRepository {
    TableMetadata createTable(CreateTableMetadataDto tableDto);
    TableMetadata addColumn(Long tableId, CreateColumnMetadataDto columnDto);

    TableMetadata addColumn(String tablePhysicalName, CreateColumnMetadataDto columnDto);
    TableMetadata getTable(Long tableId);

    TableMetadata getTable(String tablePhysicalName);
    List<TableMetadata> getAllTables();
    void deleteTable(Long tableId);
    void deleteColumn(Long tableId, Long columnId);
}
