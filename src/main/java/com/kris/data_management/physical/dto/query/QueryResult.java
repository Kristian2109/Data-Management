package com.kris.data_management.physical.dto.query;

import com.kris.data_management.physical.dto.record.Record;

import java.util.List;

public record QueryResult(
    Long totalRecords,
    List<Record> records
) { }
