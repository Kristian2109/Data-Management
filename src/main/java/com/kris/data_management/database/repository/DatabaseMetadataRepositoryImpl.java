package com.kris.data_management.database.repository;

import com.kris.data_management.database.dto.DatabaseMetadata;
import com.kris.data_management.database.entities.DatabaseMetadataEntity;
import com.kris.data_management.utils.DatabaseMetadataMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class DatabaseMetadataRepositoryImpl implements DatabaseMetadataRepository {
    private final DatabaseMetadataRepositoryJpa jpaRepository;

    @Autowired
    public DatabaseMetadataRepositoryImpl(DatabaseMetadataRepositoryJpa jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public DatabaseMetadata create(String physicalName, String displayName) {
        DatabaseMetadataEntity entity = new DatabaseMetadataEntity();
        entity.setPhysicalName(physicalName);
        entity.setDisplayName(displayName);
        DatabaseMetadataEntity saved = jpaRepository.save(entity);
        return DatabaseMetadataMapper.toDto(saved);
    }

    @Override
    public List<DatabaseMetadata> getAll() {
        return jpaRepository.findAll().stream()
                .map(DatabaseMetadataMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public DatabaseMetadata get(Long id) {
        return jpaRepository.findById(id)
                .map(DatabaseMetadataMapper::toDto)
                .orElse(null);
    }
}
