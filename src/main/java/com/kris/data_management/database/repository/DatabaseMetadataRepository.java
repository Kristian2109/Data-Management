package com.kris.data_management.database.repository;

import com.kris.data_management.database.dto.DatabaseMetadata;
import java.util.List;
import java.util.Optional;

public interface DatabaseMetadataRepository {
    DatabaseMetadata create(String physicalName, String displayName);
    List<DatabaseMetadata> getAll();
    DatabaseMetadata get(Long id);
}
