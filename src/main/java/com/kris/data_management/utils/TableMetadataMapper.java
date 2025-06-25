package com.kris.data_management.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kris.data_management.common.CreateTableViewDto;
import com.kris.data_management.common.exception.InternalServerError;
import com.kris.data_management.database.DatabaseContext;
import com.kris.data_management.logical.entities.ColumnMetadataEntity;
import com.kris.data_management.logical.entities.TableMetadataEntity;
import com.kris.data_management.logical.entities.ViewMetadataEntity;
import com.kris.data_management.logical.table.ColumnMetadata;
import com.kris.data_management.logical.table.CreateColumnMetadataDto;
import com.kris.data_management.logical.table.CreateTableMetadataDto;
import com.kris.data_management.logical.table.TableMetadata;
import com.kris.data_management.logical.table.ViewMetadata;
import com.kris.data_management.physical.query.PhysicalQuery;

public class TableMetadataMapper {
        private static final ObjectMapper objectMapper = new ObjectMapper();
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
                try {
                        PhysicalQuery query = objectMapper.readValue(entity.getQuery(), PhysicalQuery.class);
                        return new ViewMetadata(entity.getId(), entity.getName(), query);
                } catch (JsonProcessingException e) {
                        throw new InternalServerError("Cannot parse query from the database", e);
                }
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

        public static TableMetadataEntity fromDomain(TableMetadata domain) {
                List<ColumnMetadataEntity> columns = domain.getColumns().stream()
                                .map(TableMetadataMapper::fromDomain)
                                .collect(Collectors.toList());
                List<ViewMetadataEntity> views = domain.getViews().stream()
                                .map(TableMetadataMapper::fromDomain)
                                .collect(Collectors.toList());
                return new TableMetadataEntity(
                                domain.getId(),
                                domain.getDisplayName(),
                                domain.getPhysicalName(),
                                domain.getPhysicalDatabaseName(),
                                columns,
                                views
                );
        }

        public static ColumnMetadataEntity fromDomain(ColumnMetadata domain) {
                return new ColumnMetadataEntity(
                                domain.getId(),
                                domain.getDisplayName(),
                                domain.getPhysicalName(),
                                domain.getType()
                );
        }

        public static ViewMetadataEntity fromDomain(ViewMetadata domain) {
                try {
                        String queryJson = objectMapper.writeValueAsString(domain.query());
                        return new ViewMetadataEntity(domain.id(), domain.name(), queryJson);
                } catch (JsonProcessingException e) {
                        throw new InternalServerError("Cannot serialize query to the database", e);
                }
        }

        public static ViewMetadataEntity fromCreateDto(CreateTableViewDto domain) {
                try {
                        String queryJson = objectMapper.writeValueAsString(domain.query());
                        return new ViewMetadataEntity(null, domain.name(), queryJson);
                } catch (JsonProcessingException e) {
                        throw new InternalServerError("Cannot serialize query to the database", e);
                }
        }
}