package com.kris.data_management.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.kris.data_management.database.DatabaseContext;
import com.kris.data_management.logical.entities.ColumnMetadataEntity;
import com.kris.data_management.logical.entities.TableMetadataEntity;
import com.kris.data_management.logical.entities.ViewMetadataEntity;
import com.kris.data_management.logical.table.ColumnMetadata;
import com.kris.data_management.logical.table.CreateColumnMetadataDto;
import com.kris.data_management.logical.table.CreateTableMetadataDto;
import com.kris.data_management.logical.table.TableMetadata;
import com.kris.data_management.logical.table.ViewMetadata;

public class TableMetadataMapper {
        public static TableMetadata toDomain(TableMetadataEntity entity) {
                List<ColumnMetadata> columns = entity.getColumns().stream()
                                .map(TableMetadataMapper::toDomain)
                                .collect(Collectors.toList());
                List<ViewMetadata> views = entity.getViews().stream()
                                .map(TableMetadataMapper::toDomain)
                                .collect(Collectors.toList());
                return new TableMetadata(
                                entity.getId(),
                                entity.getDisplayName(),
                                entity.getPhysicalName(),
                                entity.getPhysicalDatabaseName(),
                                columns,
                                views);
        }

        public static ColumnMetadata toDomain(ColumnMetadataEntity entity) {
                return new ColumnMetadata(
                                entity.getId(),
                                entity.getDisplayName(),
                                entity.getPhysicalName(),
                                entity.getType());
        }

        public static ViewMetadata toDomain(ViewMetadataEntity entity) {
                return new ViewMetadata(
                                entity.getId(),
                                entity.getName(),
                                null);
        }

        public static TableMetadataEntity fromCreateDto(CreateTableMetadataDto dto) {
                List<ColumnMetadataEntity> columns = dto.columns().stream()
                                .map(TableMetadataMapper::fromCreateDto)
                                .collect(Collectors.toList());
                return new TableMetadataEntity(
                                null,
                                dto.displayName(),
                                dto.physicalName(),
                                DatabaseContext.getCurrentDatabase(),
                                columns,
                                new ArrayList<>());
        }

        public static ColumnMetadataEntity fromCreateDto(CreateColumnMetadataDto dto) {
                return new ColumnMetadataEntity(null, dto.displayName(), dto.physicalName(), dto.type());
        }
}