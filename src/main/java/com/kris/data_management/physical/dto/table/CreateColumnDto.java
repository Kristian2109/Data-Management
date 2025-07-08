package com.kris.data_management.physical.dto.table;

public record CreateColumnDto(
        String displayName,
        ColumnDataType type
) {

}
