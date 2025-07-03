package com.kris.data_management.logical.table;

import com.kris.data_management.physical.query.PhysicalQuery;

import java.util.Map;

public record CreateTableViewDto(String name, PhysicalQuery query, Map<String, String> physicalToDisplayColumnNames) { }
