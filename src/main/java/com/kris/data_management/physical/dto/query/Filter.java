package com.kris.data_management.physical.dto.query;

public record Filter(
    String columnName,
    String tableName,
    FilterOperator operator,
    String value
) {
} 