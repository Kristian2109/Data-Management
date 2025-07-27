package com.kris.data_management.logical.table;

import com.kris.data_management.common.exception.ValidDisplayName;
import jakarta.validation.constraints.NotBlank;

public record CreateDefaultRelationshipDto(
    @ValidDisplayName
    String childForeignKeyColumnDisplayName,

    @NotBlank
    String parentTableName,

    @NotBlank
    String childTableName
) { }
