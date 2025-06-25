package com.kris.data_management.common;

import com.kris.data_management.physical.query.PhysicalQuery;

public record CreateTableViewDto(String name, PhysicalQuery query) { }
