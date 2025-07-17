package com.kris.data_management.physical.dto.table;

import com.kris.data_management.common.exception.ValidDisplayName;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreateTableDto(
    @ValidDisplayName()
    String displayName,

    @NotNull
    @Valid
    List<CreateColumnDto> columns
) {
}
