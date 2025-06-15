package com.kris.data_management.physical.dto;

import java.util.List;

public record CreatePhysicalTableDto(
    String name,
    List<CreatePhysicalColumnDto> columns
) {
} 