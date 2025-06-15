package com.kris.data_management.database.repository;

import com.kris.data_management.database.entities.DatabaseMetadataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DatabaseMetadataRepositoryJpa extends JpaRepository<DatabaseMetadataEntity, Long> {
    Optional<DatabaseMetadataEntity> findByPhysicalName(String physicalName);
}
