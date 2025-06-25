package com.kris.data_management.logical.table;

import com.kris.data_management.physical.query.PhysicalQuery;

public record ViewMetadata(
    Long id,
    String name,
    PhysicalQuery query
) { }
