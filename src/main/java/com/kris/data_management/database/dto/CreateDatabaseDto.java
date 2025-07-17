package com.kris.data_management.database.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreateDatabaseDto {
    @NotBlank(message = "Database name is required")
    @Size(max = 100, message = "Database Name must be less than 100 characters")
    private String displayName;

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
} 