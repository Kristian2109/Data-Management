package com.kris.data_management.common;

import java.util.Optional;

public record CreateColumnDto(
        String displayName,
        ColumnDataType type,
        Optional<ParentIdentifier> parent
) {

}
