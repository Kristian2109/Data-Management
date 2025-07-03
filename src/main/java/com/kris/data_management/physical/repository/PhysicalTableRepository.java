package com.kris.data_management.physical.repository;

import com.kris.data_management.physical.dto.CreateColumnDto;
import com.kris.data_management.physical.dto.CreateRecordDto;
import com.kris.data_management.physical.dto.CreateTableDto;
import com.kris.data_management.physical.query.QueryResult;
import com.kris.data_management.physical.dto.CreatePhysicalTableResult;
import com.kris.data_management.physical.query.PhysicalQuery;

import java.util.List;

public interface PhysicalTableRepository {
    CreatePhysicalTableResult createTable(CreateTableDto tableDto);

    String addColumn(String tableName, CreateColumnDto column);

    void addRecord(String tableName, CreateRecordDto recordDto);

    void addRecords(String tableName, List<String> columnNames, List<List<String>> records);

    QueryResult executeQuery(String tableName, PhysicalQuery query);
}
