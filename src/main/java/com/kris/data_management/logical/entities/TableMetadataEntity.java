package com.kris.data_management.logical.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import jakarta.persistence.JoinColumn;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "table_metadata")
public class TableMetadataEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "display_name", nullable = false)
    private String displayName;

    @Column(name = "physical_name", nullable = false)
    private String physicalName;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "table_id")
    private List<ColumnMetadataEntity> columns = new ArrayList<>();

    @OneToMany(mappedBy = "table", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ViewMetadataEntity> views = new ArrayList<>();

    @Column(name = "physical_database_name", nullable = false)
    private String physicalDatabaseName;

    public TableMetadataEntity() {}

    public TableMetadataEntity(Long id, String displayName, String physicalName, String physicalDatabaseName, List<ColumnMetadataEntity> columns, List<ViewMetadataEntity> views) {
        this.id = id;
        this.displayName = displayName;
        this.physicalName = physicalName;
        this.columns = columns;
        this.views = views;
        this.physicalDatabaseName = physicalDatabaseName;
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

    public List<ColumnMetadataEntity> getColumns() {
        return columns;
    }

    public void setColumns(List<ColumnMetadataEntity> columns) {
        this.columns = columns;
    }

    public List<ViewMetadataEntity> getViews() {
        return views;
    }

    public void addView(ViewMetadataEntity e) {
        this.views.add(e);
        e.setTable(this);
    }

    public void setViews(List<ViewMetadataEntity> views) {
        this.views = views;
    }

    public String getPhysicalDatabaseName() {
        return physicalDatabaseName;
    }

    public void setPhysicalDatabaseName(String physicalDatabaseName) {
        this.physicalDatabaseName = physicalDatabaseName;
    }
}