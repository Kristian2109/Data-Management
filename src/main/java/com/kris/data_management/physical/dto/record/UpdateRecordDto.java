package com.kris.data_management.physical.dto.record;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record UpdateRecordDto(
    @NotNull
    List<@Valid  RecordColumnValue> columnValues) {
}
