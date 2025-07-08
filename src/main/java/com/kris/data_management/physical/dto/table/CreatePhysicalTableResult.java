package com.kris.data_management.physical.dto.table;

import java.util.Map;

public record CreatePhysicalTableResult(
        String tableName,
        Map<String, String> columnsByDisplayName) {
}
