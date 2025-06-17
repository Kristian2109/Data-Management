package com.kris.data_management.physical.query;

import com.kris.data_management.common.FilterOperator;

public record Filter(
    String columnName,
    String tableName,
    FilterOperator operator,
    String value
) {
} 