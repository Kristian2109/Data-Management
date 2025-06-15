package com.kris.data_management.physical.query;

import com.kris.data_management.common.FilterOperator;

public record Filter(
    String columnName,
    FilterOperator operator,
    String value
) {
} 