package com.kris.data_management.logical.repository.tableMetadata;

import com.kris.data_management.logical.entities.TableMetadataEntity;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TableMetadataRepositoryJpa extends JpaRepository<TableMetadataEntity, Long> {
    @NonNull Optional<TableMetadataEntity> findByPhysicalName(@NonNull String physicalName);

    @Query("SELECT " +
            "new TableMetadataEntity(t.id, t.displayName, t.physicalName, t.physicalDatabaseName) " +
            "FROM TableMetadataEntity t")
    List<TableMetadataEntity> findAllWithoutCollections();
}
