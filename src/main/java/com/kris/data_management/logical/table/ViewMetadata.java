package com.kris.data_management.logical.table;

import com.kris.data_management.logical.query.Query;

public record ViewMetadata(
    Long id,
    String name,
    Query query
) { }
