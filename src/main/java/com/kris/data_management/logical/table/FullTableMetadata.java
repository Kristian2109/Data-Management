package com.kris.data_management.logical.table;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.kris.data_management.common.exception.ResourceNotFoundException;

public class FullTableMetadata extends BaseTableMetadata {
    private List<ColumnMetadata> columns = new ArrayList<>();
    private List<ViewMetadata> views = new ArrayList<>();

    public FullTableMetadata(Long id, String displayName, String physicalName, String physicalDatabaseName,
            List<ColumnMetadata> columns, List<ViewMetadata> views) {
        super(id, displayName, physicalName, physicalDatabaseName);
        this.columns = columns;
        this.views = views;
    }

    public List<ColumnMetadata> getColumns() {
        return columns;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        FullTableMetadata that = (FullTableMetadata) o;
        return Objects.equals(getId(), that.getId()) &&
                Objects.equals(getDisplayName(), that.getDisplayName()) &&
                Objects.equals(columns, that.columns);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getDisplayName(), columns);
    }

    @Override
    public String toString() {
        return "TableMetadata{" +
                "id=" + getId() +
                ", columnName='" + getDisplayName() + '\'' +
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

    public ColumnMetadata getColumnByName(String columnName) {
        return columns.stream()
                .filter(c -> c.getPhysicalName().equals(columnName))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Column Metadata", columnName));
    }

    public ViewMetadata getViewByName(String viewName) {
        return views.stream()
            .filter(c -> c.name().equals(viewName))
            .findFirst()
            .orElseThrow(() -> new ResourceNotFoundException("View Metadata", viewName));
    }
}
