package com.kris.data_management.physical.query;

import com.kris.data_management.physical.dto.OrderDirection;

public record OrderBy(
    String columnName,
    String tableName,
    OrderDirection direction
) {
} 