package com.kris.data_management.physical.dto;

import java.util.List;

public record AddRecordsBatchDto(
    List<String> columnNames,
    List<List<String>> records
) {
}
