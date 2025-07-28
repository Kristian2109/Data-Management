package com.kris.data_management.physical.repository;

import com.kris.data_management.common.exception.TextSearchResponseDto;
import com.kris.data_management.logical.table.CreateRelationshipDto;
import com.kris.data_management.physical.dto.table.CreateColumnDto;
import com.kris.data_management.physical.dto.record.UpdateRecordDto;
import com.kris.data_management.physical.dto.table.CreateTableDto;
import com.kris.data_management.physical.dto.query.QueryResult;
import com.kris.data_management.physical.dto.table.CreatePhysicalTableResult;
import com.kris.data_management.physical.dto.query.PhysicalQuery;

import java.util.List;

public interface PhysicalTableRepository {
    CreatePhysicalTableResult createTable(CreateTableDto tableDto);

    String addColumn(String tableName, CreateColumnDto column);

    void addRecord(String tableName, UpdateRecordDto recordDto);

    void addRecords(String tableName, List<String> columnNames, List<List<String>> records);

    QueryResult executeQuery(String tableName, PhysicalQuery query);

    void addForeignKeyConstraint(CreateRelationshipDto createRelationshipDto);

    void updateRecord(String tableName, Long recordId, UpdateRecordDto recordDto);
    void deleteRecord(String tableName, Long recordId);

    void deleteTable(String tableName);

    List<List<String>> searchRecords(String tableName, String searchText, List<String> columnNames);

}
