package com.kris.data_management.common;

public record CreateColumnDto(
    String displayName,
    ColumnDataType type
) { }
