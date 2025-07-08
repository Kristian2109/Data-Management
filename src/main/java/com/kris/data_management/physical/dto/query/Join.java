package com.kris.data_management.physical.dto.query;

public record Join(
        String leftTableName,
        String leftColumnName,
        String rightTableName,
        String rightColumnName
) {
} 