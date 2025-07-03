package com.kris.data_management.logical.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "view_metadata")
public class ViewMetadataEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "query_content", nullable = false, columnDefinition = "TEXT")
    private String query;

    @Column(name = "physical_to_display_column_names", columnDefinition = "TEXT")
    private String physicalToDisplayColumnNames;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "table_id", nullable = false)
    private TableMetadataEntity table;

    public TableMetadataEntity getTable() {
        return table;
    }

    public void setTable(TableMetadataEntity table) {
        this.table = table;
    }

    public ViewMetadataEntity() {}

    public ViewMetadataEntity(Long id, String name, String query, String physicalToDisplayColumnNames,
                              TableMetadataEntity table) {
        this.id = id;
        this.name = name;
        this.query = query;
        this.physicalToDisplayColumnNames = physicalToDisplayColumnNames;
        this.table = table;
    }

    public ViewMetadataEntity(Long id, String name, String query, String physicalToDisplayColumnNames) {
        this.id = id;
        this.query = query;
        this.name = name;
        this.physicalToDisplayColumnNames = physicalToDisplayColumnNames;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhysicalToDisplayColumnNames() {
        return physicalToDisplayColumnNames;
    }

    public void setPhysicalToDisplayColumnNames(String physicalToDisplayColumnNames) {
        this.physicalToDisplayColumnNames = physicalToDisplayColumnNames;
    }


    @Override
    public String toString() {
        return "ViewMetadataEntity{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", query='" + query + '\'' +
            '}';
    }
}