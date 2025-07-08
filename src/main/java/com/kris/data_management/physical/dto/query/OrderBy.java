package com.kris.data_management.physical.dto.query;

public record OrderBy(
    String columnName,
    String tableName,
    OrderDirection direction
) {
} 