package com.kris.data_management.dtos;

import com.kris.data_management.dtos.model.table.ColumnMetadata;

public record CreatePhysicalColumnDto(
    ColumnMetadata type,
    String name
) { }
