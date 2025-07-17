package com.kris.data_management.logical.table;

import com.kris.data_management.common.exception.ValidDisplayName;

public record UpdateColumnDto(
    @ValidDisplayName
    String displayName
) { }
