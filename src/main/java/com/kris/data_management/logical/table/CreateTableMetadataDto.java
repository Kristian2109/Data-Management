package com.kris.data_management.logical.table;

import java.util.List;

public record CreateTableMetadataDto(String displayName, String physicalName, List<CreateColumnMetadataDto> columns) { }
