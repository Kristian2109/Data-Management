package com.kris.data_management.physical.dto.query;

import java.util.List;

public record PhysicalQuery(
        List<Select> select,
        List<Filter> filters,
        List<OrderBy> orders,
        Pagination pagination,
        List<Join> joins
) {
    public PhysicalQuery(PhysicalQuery other, Pagination pagination) {
        this(other.select, other.filters, other.orders, pagination, other.joins);
    }
}
