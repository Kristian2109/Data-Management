package com.kris.data_management.physical.dto.query;

import java.util.List;

public record PhysicalQuery(
        List<Select> select,
        List<Filter> filters,
        List<OrderBy> orders,
        Pagination pagination,
        List<Join> joins
) { }
