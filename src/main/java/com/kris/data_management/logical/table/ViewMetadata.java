package com.kris.data_management.logical.table;

import com.kris.data_management.physical.dto.query.PhysicalQuery;

import java.util.Map;

public record ViewMetadata(
    Long id,
    String name,
    PhysicalQuery query,
    Map<String, String> physicalToDisplayColumnNames
) { }
