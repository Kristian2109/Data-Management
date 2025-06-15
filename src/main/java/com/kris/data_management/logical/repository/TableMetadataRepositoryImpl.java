package com.kris.data_management.logical.repository;

import com.kris.data_management.logical.entities.TableMetadataEntity;
import com.kris.data_management.logical.table.ColumnMetadata;
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
    public ColumnMetadata addColumn(Long tableId, String columnName) {
        return null;
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
