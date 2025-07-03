package com.kris.data_management.logical.table;

public class BaseTableMetadata {
    private final Long id;
    private String displayName;
    private final String physicalName;
    private final String physicalDatabaseName;

    public BaseTableMetadata(Long id, String displayName, String physicalName, String physicalDatabaseName) {
        this.id = id;
        this.displayName = displayName;
        this.physicalName = physicalName;
        this.physicalDatabaseName = physicalDatabaseName;
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

    public String getPhysicalDatabaseName() {
        return physicalDatabaseName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
} 