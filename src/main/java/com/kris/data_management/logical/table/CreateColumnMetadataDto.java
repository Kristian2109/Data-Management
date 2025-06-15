package com.kris.data_management.logical.table;

import com.kris.data_management.common.ColumnDataType;

public record CreateColumnMetadataDto(
    String displayName,
    String physicalName,
    ColumnDataType type
) { }
