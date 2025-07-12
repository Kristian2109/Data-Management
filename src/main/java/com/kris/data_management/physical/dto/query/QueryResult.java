package com.kris.data_management.physical.dto.query;

import com.kris.data_management.physical.dto.record.QueryRecord;

import java.util.List;

public record QueryResult(
    Long totalRecords,
    List<QueryRecord> records
) { }
