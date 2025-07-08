package com.kris.data_management.logical.table;

public record CreateRelationshipDto(
        String name,
        String parentTableName,
        String parentColumnName,
        String childTableName,
        String childColumnName
) {
}
