package com.kris.data_management.dtos.model.query;

import java.util.List;

public record Select(
    Long tableId,
    List<Long> columnIds
) { }
