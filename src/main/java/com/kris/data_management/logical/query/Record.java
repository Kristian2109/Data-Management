package com.kris.data_management.logical.query;

import java.util.List;

public record Record(
    List<ColumnValue> columnValues
) { }
