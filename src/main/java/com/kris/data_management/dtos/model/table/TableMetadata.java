package com.kris.data_management.dtos.model.table;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TableMetadata {
    private final Long id;
    private String displayName = "";
    private List<ColumnMetadata> columns = new ArrayList<>();
    private List<ViewMetadata> views = new ArrayList<>();

    public TableMetadata(Long id, String displayName, List<ColumnMetadata> columns, List<ViewMetadata> views) {
        this.id = id;
        this.displayName = displayName;
        this.columns = columns;
        this.views = views;
    }

    public TableMetadata(Long id, String displayName) {
        this.id = id;
        this.displayName = displayName;
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
                ", displayName='" + displayName + '\'' +
                ", columns=" + columns +
                '}';
    }

    public void addColumn(ColumnMetadata column) {
        columns.add(column);
    }

    public List<ViewMetadata> getViews() {
        return views;
    }

    public void addView(ViewMetadata view) {
        views.add(view);
    }
}