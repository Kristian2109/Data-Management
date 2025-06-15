package com.kris.data_management.physical.query;

public record Join(
        String leftTableName,
        String leftColumnName,
        String rightTableName,
        String rightColumnName
) {
} 