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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "column_metadata")
@NoArgsConstructor
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

    public ColumnMetadataEntity(Long id, String displayName, String physicalName, ColumnDataType type, String parent) {
        this.id = id;
        this.displayName = displayName;
        this.physicalName = physicalName;
        this.type = type;
        this.parent = parent;
    }
    @Override
    public String toString() {
        return "ColumnMetadataEntity{" +
            "id=" + id +
            ", displayName='" + displayName + '\'' +
            ", physicalName='" + physicalName + '\'' +
            ", type=" + type +
            '}';
    }
}
