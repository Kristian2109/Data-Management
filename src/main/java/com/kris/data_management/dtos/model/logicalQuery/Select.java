package com.kris.data_management.dtos.model.logicalQuery;

import java.util.List;

public record Select(
    Long tableId,
    List<Long> columnIds
) { }
