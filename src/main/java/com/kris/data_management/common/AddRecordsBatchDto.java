package com.kris.data_management.common;

import java.util.List;

public record AddRecordsBatchDto(
    List<String> columnNames,
    List<List<String>> records
) {
}
