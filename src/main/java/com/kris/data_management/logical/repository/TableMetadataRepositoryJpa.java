package com.kris.data_management.logical.repository;

import com.kris.data_management.logical.entities.TableMetadataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TableMetadataRepositoryJpa extends JpaRepository<TableMetadataEntity, Long> {
    Optional<TableMetadataEntity> findByPhysicalName(String physicalName);

    @Query("SELECT new com.kris.data_management.logical.entities.TableMetadataEntity(t.id, t.displayName, t.physicalName, t.physicalDatabaseName, null, null) FROM TableMetadataEntity t")
    List<TableMetadataEntity> findAllWithoutCollections();
}
