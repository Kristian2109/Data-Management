package com.kris.data_management.physical.query;

import com.kris.data_management.common.OrderDirection;

public record OrderBy(
    String columnName,
    String tableName,
    OrderDirection direction
) {
} 