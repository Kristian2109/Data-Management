package com.kris.data_management.physical.query;

import java.util.List;

import com.kris.data_management.common.Pagination;

public record PhysicalQuery(
        List<Select> select,
        List<Filter> filters,
        List<OrderBy> orders,
        Pagination pagination,
        List<Join> joins,
        String tableName
) { }
