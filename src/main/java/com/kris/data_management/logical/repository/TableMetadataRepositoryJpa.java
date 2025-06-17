package com.kris.data_management.logical.repository;

import com.kris.data_management.logical.entities.TableMetadataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TableMetadataRepositoryJpa extends JpaRepository<TableMetadataEntity, Long> {
    public List<TableMetadataEntity> findAllByPhysicalDatabaseName(String physicalDatabaseName);
}
