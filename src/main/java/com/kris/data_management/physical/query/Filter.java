package com.kris.data_management.physical.query;

import com.kris.data_management.physical.dto.FilterOperator;

public record Filter(
    String columnName,
    String tableName,
    FilterOperator operator,
    String value
) {
} 