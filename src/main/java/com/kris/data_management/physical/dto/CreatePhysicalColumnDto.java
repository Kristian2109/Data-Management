package com.kris.data_management.physical.dto;

import com.kris.data_management.common.ColumnDataType;
import com.kris.data_management.common.DatabaseColumnType;

public record CreatePhysicalColumnDto(
    DatabaseColumnType type,
    String name
) { }
