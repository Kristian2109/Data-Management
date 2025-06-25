package com.kris.data_management.physical.query;

import com.kris.data_management.physical.dto.Record;

import java.util.List;

public record QueryResult(
    Long totalRecords,
    List<Record> records
) { }
