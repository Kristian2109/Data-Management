package com.kris.data_management.logical.repository.relationship;

import com.kris.data_management.logical.entities.RelationshipEntity;
import com.kris.data_management.logical.entities.TableMetadataEntity;
import com.kris.data_management.logical.table.BaseTableMetadata;
import com.kris.data_management.logical.table.ColumnMetadata;
import com.kris.data_management.logical.table.FullTableMetadata;
import com.kris.data_management.logical.table.Relationship;
import com.kris.data_management.mappers.RelationshipMapper;
import com.kris.data_management.mappers.TableMetadataMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class RelationshipRepositoryImpl implements RelationshipRepository{

    public RelationshipRepositoryImpl(RelationshipRepositoryJpa relationshipRepository) {
        this.relationshipRepository = relationshipRepository;
    }

    private final RelationshipRepositoryJpa relationshipRepository;

    @Override
    public Relationship getById(Long id) {
        return null;
    }

    @Override
    public Relationship create(String name, FullTableMetadata parentTable, FullTableMetadata childTable, String parentColumnName, String childColumnName) {
        TableMetadataEntity parentTableEntity = TableMetadataMapper.fromDomain(parentTable);
        TableMetadataEntity childTableEntity = TableMetadataMapper.fromDomain(childTable);

        RelationshipEntity relationship = relationshipRepository.save(new RelationshipEntity(null,
                name,
                parentTableEntity.getColumnByName(parentColumnName),
                childTableEntity.getColumnByName(childColumnName))
        );

        return RelationshipMapper.toDomain(relationship);

    }

    @Override
    public List<Relationship> getAll() {
        return relationshipRepository.findAll().stream()
                .map(RelationshipMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Relationship> getByTableId() {
        return null;
    }
}
