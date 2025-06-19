package com.kris.data_management.physical.repository;

import com.kris.data_management.common.CreateColumnDto;
import com.kris.data_management.common.CreateTableDto;
import com.kris.data_management.physical.query.QueryResult;
import com.kris.data_management.physical.dto.CreatePhysicalTableResult;
import com.kris.data_management.physical.query.PhysicalQuery;

import java.util.List;
import java.util.Map;

public interface PhysicalTableRepository {
    CreatePhysicalTableResult createTable(CreateTableDto tableDto);

    String addColumn(String tableName, CreateColumnDto column);

    void addRecord(String tableName, Map<String, String> valuePerColumn);

    void addRecords(String tableName, List<Map<String, String>> records);

    QueryResult executeQuery(PhysicalQuery query);
}
