package com.kris.data_management.logical.entities;

import com.kris.data_management.logical.table.Relationship;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "relationship_metadata")
public class RelationshipEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne(targetEntity = ColumnMetadataEntity.class)
    private ColumnMetadataEntity parentColumn;

    @ManyToOne(targetEntity = ColumnMetadataEntity.class)
    private ColumnMetadataEntity childColumn;

    @Override
    public String toString() {
        return "RelationshipEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
