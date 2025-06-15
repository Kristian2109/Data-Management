package com.kris.data_management.dtos.model.physicalQuery;

public record Join(
        String leftTableName,
        String leftColumnName,
        String rightTableName,
        String rightColumnName
) {
} 