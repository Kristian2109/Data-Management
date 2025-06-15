package com.kris.data_management.repositories;

import com.kris.data_management.dtos.model.DatabaseMetadata;

import java.util.List;

public interface DatabaseMetadataRepository {
    DatabaseMetadata create(String physicalName, String displayName);
    List<DatabaseMetadata> getAll();
    DatabaseMetadata get(Long id);
}
