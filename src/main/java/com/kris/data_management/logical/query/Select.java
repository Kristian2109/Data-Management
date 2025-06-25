package com.kris.data_management.logical.query;

import java.util.List;

public record Select(
        Long tableId,
        List<Long> columnIds) {
}
