package com.kris.data_management.logical.table;

import jakarta.validation.constraints.NotBlank;

@NotBlank
public record CreateRelationshipDto(
        String name,
        String parentTableName,
        String parentColumnName,
        String childTableName,
        String childColumnName
) {
}
