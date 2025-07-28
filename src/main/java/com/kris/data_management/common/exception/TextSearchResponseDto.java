package com.kris.data_management.common.exception;

import java.util.List;

public record TextSearchResponseDto (
    List<ColumnDto> columns,
    List<List<String>> records
) {
    public record ColumnDto(
        String displayName,
        String physicalName
    ) {}
}
