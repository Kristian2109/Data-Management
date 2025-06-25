package com.kris.data_management.common;

import java.util.List;

public record CreateRecordDto(
        List<RecordColumnValue> columnValues) {
}
