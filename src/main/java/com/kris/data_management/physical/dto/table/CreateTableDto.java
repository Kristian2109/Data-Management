package com.kris.data_management.physical.dto.table;

import java.util.List;

public record CreateTableDto(
    String displayName,
    List<CreateColumnDto> columns
) {
}
