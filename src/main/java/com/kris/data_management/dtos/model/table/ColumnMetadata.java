package com.kris.data_management.dtos.model.table;

public class ColumnMetadata {
    private final Long id;
    private final String displayName;
    private final String physicalName;
    private final String type;

    public ColumnMetadata(Long id, String displayName, String physicalName, String type) {
        this.id = id;
        this.displayName = displayName;
        this.physicalName = physicalName;
        this.type = type;
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

    public String getType() {
        return type;
    }
}