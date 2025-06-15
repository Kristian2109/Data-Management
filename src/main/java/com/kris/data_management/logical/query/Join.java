package com.kris.data_management.logical.query;

public record Join(
    Long firstTableId,
    Long secondTableId,
    Long firstTableColumnId,
    Long secondTableColumnId
) { }
