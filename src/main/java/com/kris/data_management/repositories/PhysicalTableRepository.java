package com.kris.data_management.repositories;

import com.kris.data_management.dtos.CreatePhysicalColumnDto;
import com.kris.data_management.dtos.model.logicalQuery.QueryResult;
import com.kris.data_management.dtos.model.physicalQuery.PhysicalQuery;

import java.util.List;
import java.util.Map;

public interface PhysicalTableRepository {
    void createTable(String tableName, List<CreatePhysicalColumnDto> columns);
    void addColumn(String tableName, CreatePhysicalColumnDto column);
    void addRecord(String tableName, Map<String, String> valuePerColumn);
    void addRecords(String tableName, List<Map<String, String>> records);
    QueryResult executeQuery(PhysicalQuery query);
}
