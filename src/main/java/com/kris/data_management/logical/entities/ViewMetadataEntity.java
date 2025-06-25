package com.kris.data_management.logical.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "view_metadata")
public class ViewMetadataEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "query_content", nullable = false, columnDefinition = "json")
    private String query;

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

    public ViewMetadataEntity(Long id, String name, String query, TableMetadataEntity table) {
        this.id = id;
        this.name = name;
        this.query = query;
        this.table = table;
    }

    public ViewMetadataEntity(Long id, String name, String query) {
        this.id = id;
        this.query = query;
        this.name = name;
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

    @Override
    public String toString() {
        return "ViewMetadataEntity{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", query='" + query + '\'' +
            '}';
    }
}