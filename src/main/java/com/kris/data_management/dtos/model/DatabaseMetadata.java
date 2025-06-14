package com.kris.data_management.dtos.model;

public class DatabaseMetadata {
    public DatabaseMetadata(Long id, String name, String physicalName) {
        this.id = id;
        this.displayName = name;
        this.physicalName = physicalName;
    }

    public Long getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getPhysicalName() {
        return physicalName;
    }

    private final Long id;
    private final String displayName;

    private final String physicalName;
}
