package com.kris.data_management.dtos.model.query;

public record Join(
    Long firstTableId,
    Long secondTableId,
    Long firstTableColumnId,
    Long secondTableColumnId
) { }
