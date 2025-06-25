package com.kris.data_management.logical.repository;

import com.kris.data_management.common.CreateTableViewDto;
import com.kris.data_management.common.exception.ResourceNotFoundException;
import com.kris.data_management.logical.entities.TableMetadataEntity;
import com.kris.data_management.logical.entities.ViewMetadataEntity;
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
    public TableMetadata addColumn(String tablePhysicalName, CreateColumnMetadataDto columnDto) {
        TableMetadataEntity entity = repositoryJpa.findByPhysicalName(tablePhysicalName)
            .orElseThrow(() -> new ResourceNotFoundException("Table Metadata", tablePhysicalName));

        entity.getColumns().add(TableMetadataMapper.fromCreateDto(columnDto));
        entity = repositoryJpa.save(entity);
        return TableMetadataMapper.toDomain(entity);
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
    public TableMetadata save(TableMetadata table) {
        TableMetadataEntity saved = repositoryJpa.save(TableMetadataMapper.fromDomain(table));
        return TableMetadataMapper.toDomain(saved);
    }

    @Override
    public TableMetadata addView(String tablePhysicalName, CreateTableViewDto viewDto) {
        TableMetadataEntity entity = repositoryJpa.findByPhysicalName(tablePhysicalName)
            .orElseThrow(() -> new ResourceNotFoundException("Table Metadata", tablePhysicalName));

        ViewMetadataEntity viewMetadata = TableMetadataMapper.fromCreateDto(viewDto);
        entity.addView(viewMetadata);
        entity = repositoryJpa.save(entity);
        return TableMetadataMapper.toDomain(entity);
    }

    @Override
    public void deleteTable(Long tableId) {

    }

    @Override
    public void deleteColumn(Long tableId, Long columnId) {

    }
}
