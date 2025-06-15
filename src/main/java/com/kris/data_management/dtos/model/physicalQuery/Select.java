package com.kris.data_management.dtos.model.physicalQuery;

public record Select(
    String columnName,
    String aggregation
) {
} 