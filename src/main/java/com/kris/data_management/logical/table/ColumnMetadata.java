package com.kris.data_management.logical.table;

import com.kris.data_management.physical.dto.ColumnDataType;
import com.kris.data_management.physical.dto.ParentIdentifier;

import java.util.Optional;

public class ColumnMetadata {
    private final Long id;
    private final String displayName;
    private final String physicalName;
    private final ColumnDataType type;
    private Optional<ParentIdentifier>  parent = Optional.empty();

    public ColumnMetadata(Long id, String displayName, String physicalName, ColumnDataType type, ParentIdentifier parent) {
        this.id = id;
        this.displayName = displayName;
        this.physicalName = physicalName;
        this.type = type;
        this.parent = Optional.of(parent);
    }

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

    public Optional<ParentIdentifier> getParent() {
        return parent;
    }

    public void setParent(ParentIdentifier parent) {
        this.parent = Optional.of(parent);
    }
}