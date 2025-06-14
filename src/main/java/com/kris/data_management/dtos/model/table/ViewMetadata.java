package com.kris.data_management.dtos.model.table;

import com.kris.data_management.dtos.model.query.Query;

public record ViewMetadata(
    Long id,
    String name,
    Query query
) { }
