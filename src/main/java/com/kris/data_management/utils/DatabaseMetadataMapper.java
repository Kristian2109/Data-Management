package com.kris.data_management.utils;

import com.kris.data_management.database.entities.DatabaseMetadataEntity;
import com.kris.data_management.database.dto.DatabaseMetadata;

public class DatabaseMetadataMapper {
    public static DatabaseMetadata toDto(DatabaseMetadataEntity entity) {
        if (entity == null) return null;
        return new DatabaseMetadata(
            entity.getId(),
            entity.getDisplayName(),
            entity.getPhysicalName()
        );
    }

    public static DatabaseMetadataEntity toEntity(DatabaseMetadata dto) {
        if (dto == null) return null;
        DatabaseMetadataEntity entity = new DatabaseMetadataEntity();
        entity.setId(dto.getId());
        entity.setDisplayName(dto.getDisplayName());
        entity.setPhysicalName(dto.getPhysicalName());
        return entity;
    }
} 