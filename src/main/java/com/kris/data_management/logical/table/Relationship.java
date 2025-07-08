package com.kris.data_management.logical.table;

public record Relationship(
        Long id,
        String name,
        String parentTableName,
        String parentColumnName,
        String childTableName,
        String childColumnName
) {}
