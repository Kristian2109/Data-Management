package com.kris.data_management.services;

import com.kris.data_management.logical.repository.relationship.RelationshipRepository;
import com.kris.data_management.logical.repository.tableMetadata.TableMetadataRepository;
import com.kris.data_management.logical.table.CreateColumnMetadataDto;
import com.kris.data_management.logical.table.CreateDefaultRelationshipDto;
import com.kris.data_management.logical.table.CreateRelationshipDto;
import com.kris.data_management.logical.table.FullTableMetadata;
import com.kris.data_management.logical.table.Relationship;
import com.kris.data_management.physical.dto.table.ColumnDataType;
import com.kris.data_management.physical.dto.table.CreateColumnDto;
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

    @Transactional
    public Relationship createDefaultRelationship(CreateDefaultRelationshipDto dto) {
        String physicalName = this.physicalTableRepository.addColumn(dto.childTableName(),
            new CreateColumnDto(dto.childForeignKeyColumnDisplayName(), ColumnDataType.NUMBER));

        this.tableMetadataRepository.addColumn(dto.childTableName(),
            new CreateColumnMetadataDto(dto.childForeignKeyColumnDisplayName(), physicalName, ColumnDataType.NUMBER));

        FullTableMetadata parentTable = tableMetadataRepository.getTable(dto.parentTableName());
        FullTableMetadata childTable = tableMetadataRepository.getTable(dto.childTableName());

        return relationshipRepository.create(dto.childForeignKeyColumnDisplayName(), parentTable, childTable, "id", physicalName);
    }

    @Transactional(readOnly = true)
    public List<Relationship> getAllRelationships() {
        return this.relationshipRepository.getAll();
    }

    @Transactional(readOnly = true)
    public List<Relationship> getTableRelationships(String tableId) {
        return this.relationshipRepository.getAll();
    }
}
