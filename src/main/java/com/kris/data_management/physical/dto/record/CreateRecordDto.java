package com.kris.data_management.physical.dto.record;

import java.util.List;

public record CreateRecordDto(
    List<RecordColumnValue> columnValues) {
}
