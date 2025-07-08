package com.kris.data_management.services;

import com.kris.data_management.logical.repository.relationship.RelationshipRepository;
import com.kris.data_management.logical.repository.tableMetadata.TableMetadataRepository;
import com.kris.data_management.logical.table.CreateRelationshipDto;
import com.kris.data_management.logical.table.FullTableMetadata;
import com.kris.data_management.logical.table.Relationship;
import com.kris.data_management.physical.repository.PhysicalTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RelationshipService {
    private final PhysicalTableRepository physicalTableRepository;
    private final TableMetadataRepository tableMetadataRepository;
    private final RelationshipRepository relationshipRepository;

    public RelationshipService(PhysicalTableRepository physicalTableRepository, TableMetadataRepository tableMetadataRepository, RelationshipRepository relationshipRepository) {
        this.physicalTableRepository = physicalTableRepository;
        this.tableMetadataRepository = tableMetadataRepository;
        this.relationshipRepository = relationshipRepository;
    }

    @Transactional
    public Relationship createRelationship(CreateRelationshipDto createRelationshipDto) {
        FullTableMetadata parentTable = tableMetadataRepository.getTable(createRelationshipDto.parentTableName());
        FullTableMetadata childTable = tableMetadataRepository.getTable(createRelationshipDto.childTableName());

        this.physicalTableRepository.addForeignKeyConstraint(createRelationshipDto);

        return relationshipRepository.create(
                createRelationshipDto.name(), parentTable, childTable,
                createRelationshipDto.parentColumnName(), createRelationshipDto.childColumnName()
        );
    }

    @Transactional(readOnly = true)
    public List<Relationship> getAllRelationships() {
        return this.relationshipRepository.getAll();
    }
}
