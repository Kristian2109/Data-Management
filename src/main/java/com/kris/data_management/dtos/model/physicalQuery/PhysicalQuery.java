package com.kris.data_management.dtos.model.physicalQuery;

import java.util.List;

import com.kris.data_management.dtos.model.logicalQuery.Pagination;

public record PhysicalQuery(
        List<Select> select,
        List<Filter> filters,
        List<OrderBy> orders,
        Pagination pagination,
        List<Join> joins
) {
}
