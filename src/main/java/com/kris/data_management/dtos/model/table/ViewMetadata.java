package com.kris.data_management.dtos.model.table;

import com.kris.data_management.dtos.model.logicalQuery.Query;

public record ViewMetadata(
    Long id,
    String name,
    Query query
) { }
