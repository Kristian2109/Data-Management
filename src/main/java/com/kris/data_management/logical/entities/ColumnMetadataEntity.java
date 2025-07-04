package com.kris.data_management.logical.entities;

import com.kris.data_management.physical.dto.ColumnDataType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "column_metadata")
public class ColumnMetadataEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "display_name", nullable = false)
    private String displayName;

    @Column(name = "physical_name", nullable = false)
    private String physicalName;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private ColumnDataType type;

    @Column(name = "parent", columnDefinition = "TEXT")
    private String parent;

    public ColumnMetadataEntity() {}

    public ColumnMetadataEntity(Long id, String displayName, String physicalName, ColumnDataType type, String parent) {
        this.id = id;
        this.displayName = displayName;
        this.physicalName = physicalName;
        this.type = type;
        this.parent = parent;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getPhysicalName() {
        return physicalName;
    }

    public void setPhysicalName(String physicalName) {
        this.physicalName = physicalName;
    }

    public ColumnDataType getType() {
        return type;
    }

    public void setType(ColumnDataType type) {
        this.type = type;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }
}
