package com.kris.data_management.physical.dto.record;

import jakarta.validation.constraints.NotBlank;

public record RecordColumnValue(
    @NotBlank
    String columnName,
    @NotBlank
    String stringValue
) { }
