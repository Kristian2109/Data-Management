package com.kris.data_management.logical.query;

public record OrderBy (
    Long columnId,
    OrderDirection direction,
    Long tableId
) {}
