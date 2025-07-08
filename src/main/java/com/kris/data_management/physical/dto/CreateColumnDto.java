package com.kris.data_management.physical.dto;

public record CreateColumnDto(
        String displayName,
        ColumnDataType type
) {

}
