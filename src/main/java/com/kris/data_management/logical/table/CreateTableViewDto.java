package com.kris.data_management.logical.table;

import com.kris.data_management.common.exception.ValidDisplayName;
import com.kris.data_management.physical.dto.query.PhysicalQuery;

import java.util.Map;

public record CreateTableViewDto(
    @ValidDisplayName
    String name,
    PhysicalQuery query,
    Map<String, String> physicalToDisplayColumnNames
) { }
