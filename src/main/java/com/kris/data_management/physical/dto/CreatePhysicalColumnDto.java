package com.kris.data_management.physical.dto;

import com.kris.data_management.logical.table.ColumnMetadata;

public record CreatePhysicalColumnDto(
    ColumnMetadata type,
    String name
) { }
