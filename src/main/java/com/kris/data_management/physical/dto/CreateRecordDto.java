package com.kris.data_management.physical.dto;

import java.util.List;

public record CreateRecordDto(
    List<RecordColumnValue> columnValues) {
}
