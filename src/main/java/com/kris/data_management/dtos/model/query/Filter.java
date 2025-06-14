package com.kris.data_management.dtos.model.query;

public record Filter(
    FilterOperator operator,
    Object value,
    Long columnId,
    Long tableId
) { }