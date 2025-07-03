package com.kris.data_management.physical.dto;

import com.kris.data_management.physical.dto.CreateColumnDto;

import java.util.List;

public record CreateTableDto(
    String displayName,
    List<CreateColumnDto> columns
) {
}
