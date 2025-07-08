package com.kris.data_management.logical.table;

import com.kris.data_management.physical.dto.ColumnDataType;

public record CreateColumnMetadataDto(
        String displayName,
        String physicalName,
        ColumnDataType type
) { }
