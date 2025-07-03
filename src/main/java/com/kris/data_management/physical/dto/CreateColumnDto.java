package com.kris.data_management.physical.dto;

import java.util.Optional;

public record CreateColumnDto(
        String displayName,
        ColumnDataType type,
        Optional<ParentIdentifier> parent
) {

}
