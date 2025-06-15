package com.kris.data_management.logical.query;

import com.kris.data_management.common.FilterOperator;

public record Filter(
    FilterOperator operator,
    Object value,
    Long columnId,
    Long tableId
) { }