package com.kris.data_management.logical.query;

import com.kris.data_management.common.OrderDirection;

public record OrderBy (
    Long columnId,
    OrderDirection direction,
    Long tableId
) {}
