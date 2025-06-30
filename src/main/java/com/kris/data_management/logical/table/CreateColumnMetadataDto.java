package com.kris.data_management.logical.table;

import com.kris.data_management.common.ColumnDataType;
import com.kris.data_management.common.ParentIdentifier;

import java.util.Optional;

public record CreateColumnMetadataDto(
        String displayName,
        String physicalName,
        ColumnDataType type,
        Optional<ParentIdentifier> parent
) { }
