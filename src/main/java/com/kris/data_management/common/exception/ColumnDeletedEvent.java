package com.kris.data_management.common.exception;

public record ColumnDeletedEvent(
    String tableId,
    String columnId
) { }
