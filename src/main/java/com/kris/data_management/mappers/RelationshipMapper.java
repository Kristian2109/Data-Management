package com.kris.data_management.mappers;

import com.kris.data_management.logical.entities.RelationshipEntity;
import com.kris.data_management.logical.entities.ColumnMetadataEntity;
import com.kris.data_management.logical.table.Relationship;

public class RelationshipMapper {

    public static Relationship toDomain(RelationshipEntity entity) {
        if (entity == null) {
            return null;
        }

        ColumnMetadataEntity parentCol = entity.getParentColumn();
        ColumnMetadataEntity childCol = entity.getChildColumn();

        return new Relationship(
                entity.getId(),
                entity.getName(),
                parentCol.getTable().getPhysicalName(),
                parentCol.getPhysicalName(),
                childCol.getTable().getPhysicalName(),
                childCol.getPhysicalName()
        );
    }
}
