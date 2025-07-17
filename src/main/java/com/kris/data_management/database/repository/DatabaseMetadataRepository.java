package com.kris.data_management.database.repository;

import com.kris.data_management.database.dto.DatabaseMetadata;
import com.kris.data_management.database.dto.UpdateDatabaseDto;

import javax.xml.crypto.Data;
import java.util.List;

public interface DatabaseMetadataRepository {
    DatabaseMetadata create(String physicalName, String displayName);
    List<DatabaseMetadata> getAll();
    DatabaseMetadata get(Long id);
    DatabaseMetadata get(String physicalName);

    DatabaseMetadata update(String id, UpdateDatabaseDto updateDatabaseDto);
}
