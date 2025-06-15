package com.kris.data_management.logical.query;

import java.util.List;

public record Query(
    List<Select> select,
    List<Filter> filters,
    List<OrderBy> orders,
    Pagination pagination,
    List<Join> joins
) {
}
