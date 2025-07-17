package com.kris.data_management.database.dto;

import com.kris.data_management.common.exception.ValidDisplayName;

public record UpdateDatabaseDto(
    @ValidDisplayName String displayName
) { }
