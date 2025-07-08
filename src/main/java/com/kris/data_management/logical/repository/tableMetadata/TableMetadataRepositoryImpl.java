package com.kris.data_management.logical.repository.tableMetadata;

import com.kris.data_management.logical.table.BaseTableMetadata;
import com.kris.data_management.logical.table.CreateTableViewDto;
import com.kris.data_management.common.exception.ResourceNotFoundException;
import com.kris.data_management.logical.entities.TableMetadataEntity;
import com.kris.data_management.logical.entities.ViewMetadataEntity;
import com.kris.data_management.logical.table.CreateColumnMetadataDto;
import com.kris.data_management.logical.table.CreateTableMetadataDto;
import com.kris.data_management.logical.table.FullTableMetadata;
import com.kris.data_management.mappers.TableMetadataMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TableMetadataRepositoryImpl implements TableMetadataRepository {
    private final TableMetadataRepositoryJpa repositoryJpa;

    public TableMetadataRepositoryImpl(TableMetadataRepositoryJpa repositoryJpa) {
        this.repositoryJpa = repositoryJpa;
    }

    @Override
    public FullTableMetadata createTable(CreateTableMetadataDto tableDto) {
        TableMetadataEntity entity = repositoryJpa.save(TableMetadataMapper.fromCreateDto(tableDto));
        return TableMetadataMapper.toDomain(entity);
    }

    @Override
    public FullTableMetadata addColumn(String tablePhysicalName, CreateColumnMetadataDto columnDto) {
        TableMetadataEntity entity = repositoryJpa.findByPhysicalName(tablePhysicalName)
            .orElseThrow(() -> new ResourceNotFoundException("Table Metadata", tablePhysicalName));

        entity.addColumn(TableMetadataMapper.fromCreateDto(columnDto));
        entity = repositoryJpa.save(entity);
        return TableMetadataMapper.toDomain(entity);
    }

    @Override
    public FullTableMetadata getTable(String tablePhysicalName) {
        return repositoryJpa.findByPhysicalName(tablePhysicalName)
            .map(TableMetadataMapper::toDomain)
            .orElseThrow(() -> new ResourceNotFoundException("Table Metadata", tablePhysicalName));
    }

    @Override
    public List<BaseTableMetadata> getAllTables() {
        return repositoryJpa.findAllWithoutCollections()
                .stream()
                .map(TableMetadataMapper::toDomainWithoutCollections)
                .toList();
    }

    @Override
    public FullTableMetadata save(FullTableMetadata table) {
        TableMetadataEntity saved = repositoryJpa.save(TableMetadataMapper.fromDomain(table));
        return TableMetadataMapper.toDomain(saved);
    }

    @Override
    public FullTableMetadata addView(String tablePhysicalName, CreateTableViewDto viewDto) {
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
