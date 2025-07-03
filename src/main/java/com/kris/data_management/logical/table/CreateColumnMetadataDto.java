package com.kris.data_management.logical.table;

import com.kris.data_management.physical.dto.ColumnDataType;
import com.kris.data_management.physical.dto.ParentIdentifier;

import java.util.Optional;

public record CreateColumnMetadataDto(
        String displayName,
        String physicalName,
        ColumnDataType type,
        Optional<ParentIdentifier> parent
) { }
