package com.kris.data_management.logical.table;

import com.kris.data_management.common.ColumnDataType;

public class ColumnMetadata {
    private final Long id;
    private final String displayName;
    private final String physicalName;
    private final ColumnDataType type;

    public ColumnMetadata(Long id, String displayName, String physicalName, ColumnDataType type) {
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

    public ColumnDataType getType() {
        return type;
    }
}