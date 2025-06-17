package com.kris.data_management.logical.repository;

import com.kris.data_management.logical.entities.TableMetadataEntity;
import com.kris.data_management.logical.table.CreateColumnMetadataDto;
import com.kris.data_management.logical.table.CreateTableMetadataDto;
import com.kris.data_management.logical.table.TableMetadata;
import com.kris.data_management.utils.TableMetadataMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public class TableMetadataRepositoryImpl implements TableMetadataRepository {
    private final TableMetadataRepositoryJpa repositoryJpa;

    public TableMetadataRepositoryImpl(TableMetadataRepositoryJpa repositoryJpa) {
        this.repositoryJpa = repositoryJpa;
    }

    @Override
    public TableMetadata createTable(CreateTableMetadataDto tableDto) {
        TableMetadataEntity entity = repositoryJpa.save(TableMetadataMapper.fromCreateDto(tableDto));
        return TableMetadataMapper.toDomain(entity);
    }

    @Override
    public TableMetadata addColumn(Long tableId, CreateColumnMetadataDto columnMetadataDto) {
        TableMetadataEntity entity = repositoryJpa.findById(tableId)
            .orElseThrow(() -> new EntityNotFoundException("Table with id " + tableId + " not found"));

        entity.getColumns().add(TableMetadataMapper.fromCreateDto(columnMetadataDto));
        entity = repositoryJpa.save(entity);
        return TableMetadataMapper.toDomain(entity);
    }

    @Override
    public TableMetadata getTable(Long tableId) {
        return null;
    }

    @Override
    public List<TableMetadata> getAllTables() {
        return null;
    }

    @Override
    public void deleteTable(Long tableId) {

    }

    @Override
    public void deleteColumn(Long tableId, Long columnId) {

    }
}
