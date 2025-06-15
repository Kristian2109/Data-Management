package com.kris.data_management.dtos.model.physicalQuery;

import com.kris.data_management.dtos.model.logicalQuery.OrderDirection;

public record OrderBy(
    String columnName,
    OrderDirection direction
) {
} 