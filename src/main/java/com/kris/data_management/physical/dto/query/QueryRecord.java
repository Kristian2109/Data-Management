package com.kris.data_management.physical.dto.query;

import java.util.List;

public record QueryRecord(
    List<QueryRecordColumnValue> columnValues
) { }
