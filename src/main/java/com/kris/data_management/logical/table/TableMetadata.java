package com.kris.data_management.logical.table;

import com.kris.data_management.physical.exception.ResourceNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TableMetadata {
    private final Long id;
    private String displayName;
    private final String physicalName;
    private final String physicalDatabaseName;
    private List<ColumnMetadata> columns = new ArrayList<>();
    private List<ViewMetadata> views = new ArrayList<>();

    public TableMetadata(Long id, String displayName, String physicalName, String physicalDatabaseName, List<ColumnMetadata> columns, List<ViewMetadata> views) {
        this.id = id;
        this.displayName = displayName;
        this.physicalName = physicalName;
        this.physicalDatabaseName = physicalDatabaseName;
        this.columns = columns;
        this.views = views;
    }

    public TableMetadata(Long id, String displayName, String physicalName, String physicalDatabaseName) {
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

    public List<ColumnMetadata> getColumns() {
        return columns;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TableMetadata that = (TableMetadata) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(displayName, that.displayName) &&
                Objects.equals(columns, that.columns);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, displayName, columns);
    }

    @Override
    public String toString() {
        return "TableMetadata{" +
                "id=" + id +
                ", columnName='" + displayName + '\'' +
                ", columns=" + columns +
                '}';
    }

    public void addColumn(ColumnMetadata column) {
        columns.add(column);
    }

    public List<ViewMetadata> getViews() {
        return views;
    }

    public String getPhysicalName() {
        return physicalName;
    }


    public void addView(ViewMetadata view) {
        views.add(view);
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getPhysicalDatabaseName() {
        return physicalDatabaseName;
    }

    public ColumnMetadata getColumnById(Long id) {
        return columns.stream()
            .filter(c -> c.getId().equals(id))
            .findFirst()
            .orElseThrow(() -> new ResourceNotFoundException("Column Metadata", id));
    }
}