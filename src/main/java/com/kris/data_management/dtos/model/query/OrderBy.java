package com.kris.data_management.dtos.model.query;

public record OrderBy (
    Long columnId,
    OrderDirection direction,
    Long tableId
) {}
