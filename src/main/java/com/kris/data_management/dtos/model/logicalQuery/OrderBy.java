package com.kris.data_management.dtos.model.logicalQuery;

public record OrderBy (
    Long columnId,
    OrderDirection direction,
    Long tableId
) {}
