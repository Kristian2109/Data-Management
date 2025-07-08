package com.kris.data_management.physical.dto.record;

import java.util.List;

public record Record(
    List<ColumnValue> columnValues
) { }
