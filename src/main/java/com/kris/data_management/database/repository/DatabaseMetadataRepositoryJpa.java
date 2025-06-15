package com.kris.data_management.database.repository;

import com.kris.data_management.database.entities.DatabaseMetadataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DatabaseMetadataRepositoryJpa extends JpaRepository<DatabaseMetadataEntity, Long> {
}
