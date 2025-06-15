package com.kris.data_management.physical.query;

import com.kris.data_management.logical.query.OrderDirection;

public record OrderBy(
    String columnName,
    OrderDirection direction
) {
} 