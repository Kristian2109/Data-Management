package com.kris.data_management.logical.entities;

import com.kris.data_management.common.exception.ResourceNotFoundException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.Table;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import jakarta.persistence.JoinColumn;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "table_metadata")
@NoArgsConstructor
@Getter
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

    public TableMetadataEntity(Long id, String displayName, String physicalName, String physicalDatabaseName, List<ColumnMetadataEntity> columns, List<ViewMetadataEntity> views) {
        this.id = id;
        this.displayName = displayName;
        this.physicalName = physicalName;
        this.columns = columns;
        this.views = views;
        this.physicalDatabaseName = physicalDatabaseName;

        columns.forEach(column -> {
            column.setTable(this);
        });
    }

    public TableMetadataEntity(Long id, String displayName, String physicalName, String physicalDatabaseName) {
        this.id = id;
        this.displayName = displayName;
        this.physicalName = physicalName;
        this.physicalDatabaseName = physicalDatabaseName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setPhysicalName(String physicalName) {
        this.physicalName = physicalName;
    }

    public void addView(ViewMetadataEntity e) {
        this.views.add(e);
        e.setTable(this);
    }

    public String getPhysicalDatabaseName() {
        return physicalDatabaseName;
    }

    @Override
    public String toString() {
        return "TableMetadataEntity{" +
            "id=" + id +
            ", displayName='" + displayName + '\'' +
            ", physicalName='" + physicalName + '\'' +
            '}';
    }

    public ColumnMetadataEntity getColumnByName(String columnName) {
        return this.columns.stream()
                .filter(c -> c.getPhysicalName().equals(columnName))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Column",  columnName));
    }

    public void addColumn(ColumnMetadataEntity column) {
        this.columns.add(column);
        column.setTable(this);
    }
}