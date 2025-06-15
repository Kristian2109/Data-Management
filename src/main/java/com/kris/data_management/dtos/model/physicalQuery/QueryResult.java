package com.kris.data_management.dtos.model.logicalQuery;

import java.util.List;

public record QueryResult(
    Long totalRecords,
    List<Record> records
) { }
