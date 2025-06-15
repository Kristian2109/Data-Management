package com.kris.data_management.dtos.model.physicalQuery;

import com.kris.data_management.dtos.model.logicalQuery.FilterOperator;

public record Filter(
    String columnName,
    FilterOperator operator,
    String value
) {
} 