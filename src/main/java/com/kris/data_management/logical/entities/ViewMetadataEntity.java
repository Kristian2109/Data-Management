package com.kris.data_management.logical.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "view_metadata")
public class ViewMetadataEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "table_id", nullable = false)
    private TableMetadataEntity table;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "query_content", nullable = false)
    private String query;
    
    public ViewMetadataEntity() {}

    public ViewMetadataEntity(Long id, TableMetadataEntity table, String query) {
        this.id = id;
        this.table = table;
        this.query = query;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TableMetadataEntity getTable() {
        return table;
    }

    public void setTable(TableMetadataEntity table) {
        this.table = table;
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
}