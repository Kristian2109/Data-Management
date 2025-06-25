package com.kris.data_management.logical.repository;

import com.kris.data_management.common.exception.ResourceNotFoundException;
import com.kris.data_management.logical.entities.TableMetadataEntity;
import com.kris.data_management.logical.table.CreateColumnMetadataDto;
import com.kris.data_management.logical.table.CreateTableMetadataDto;
import com.kris.data_management.logical.table.TableMetadata;
import com.kris.data_management.utils.TableMetadataMapper;
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
                .orElseThrow(() -> new ResourceNotFoundException("Table Metadata", tableId));

        entity.getColumns().add(TableMetadataMapper.fromCreateDto(columnMetadataDto));
        entity = repositoryJpa.save(entity);
        return TableMetadataMapper.toDomain(entity);
    }

    @Override
    public TableMetadata addColumn(String tablePhysicalName, CreateColumnMetadataDto columnDto) {
        TableMetadataEntity entity = repositoryJpa.findByPhysicalName(tablePhysicalName)
            .orElseThrow(() -> new ResourceNotFoundException("Table Metadata", tablePhysicalName));

        entity.getColumns().add(TableMetadataMapper.fromCreateDto(columnDto));
        entity = repositoryJpa.save(entity);
        return TableMetadataMapper.toDomain(entity);
    }

    @Override
    public TableMetadata getTable(Long tableId) {
        return repositoryJpa.findById(tableId)
                .map(TableMetadataMapper::toDomain)
                .orElseThrow(() -> new ResourceNotFoundException("Table Metadata", tableId));
    }

    @Override
    public TableMetadata getTable(String tablePhysicalName) {
        return repositoryJpa.findByPhysicalName(tablePhysicalName)
            .map(TableMetadataMapper::toDomain)
            .orElseThrow(() -> new ResourceNotFoundException("Table Metadata", tablePhysicalName));
    }

    @Override
    public List<TableMetadata> getAllTables() {
        return repositoryJpa.findAll()
                .stream()
                .map(TableMetadataMapper::toDomain)
                .toList();
    }

    @Override
    public void deleteTable(Long tableId) {

    }

    @Override
    public void deleteColumn(Long tableId, Long columnId) {

    }
}
