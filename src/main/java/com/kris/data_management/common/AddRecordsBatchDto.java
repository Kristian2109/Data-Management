package com.kris.data_management.common;

import java.util.List;

public record AddRecordsBatchDto(
    List<Long> columnIds,
    List<List<String>> records
) {
}
