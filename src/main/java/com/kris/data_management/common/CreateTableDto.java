package com.kris.data_management.common;

import java.util.List;

public record CreateTableDto(
    String displayName,
    List<CreateColumnDto> columns
) {
}
