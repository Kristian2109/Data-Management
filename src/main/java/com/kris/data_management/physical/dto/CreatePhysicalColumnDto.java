package com.kris.data_management.physical.dto;

import com.kris.data_management.common.ColumnDataType;

public record CreatePhysicalColumnDto(
        ColumnDataType type,
        String name) {
}
