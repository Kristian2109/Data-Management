package com.kris.data_management.physical.dto.table;

import com.kris.data_management.common.exception.ValidDisplayName;
import jakarta.validation.constraints.NotNull;

public record CreateColumnDto(
    @ValidDisplayName
    String displayName,

    @NotNull
    ColumnDataType type
) { }
