package com.kris.data_management.mappers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kris.data_management.logical.table.BaseTableMetadata;
import com.kris.data_management.physical.dto.CreateTableViewDto;
import com.kris.data_management.physical.dto.ParentIdentifier;
import com.kris.data_management.common.exception.InternalServerError;
import com.kris.data_management.database.DatabaseContext;
import com.kris.data_management.logical.entities.ColumnMetadataEntity;
import com.kris.data_management.logical.entities.TableMetadataEntity;
import com.kris.data_management.logical.entities.ViewMetadataEntity;
import com.kris.data_management.logical.table.ColumnMetadata;
import com.kris.data_management.logical.table.CreateColumnMetadataDto;
import com.kris.data_management.logical.table.CreateTableMetadataDto;
import com.kris.data_management.logical.table.FullTableMetadata;
import com.kris.data_management.logical.table.ViewMetadata;
import com.kris.data_management.physical.query.PhysicalQuery;

public class TableMetadataMapper {
        private static final ObjectMapper objectMapper = new ObjectMapper();
        public static FullTableMetadata toDomain(TableMetadataEntity entity) {
                List<ColumnMetadata> columns = entity.getColumns().stream()
                                .map(TableMetadataMapper::toDomain)
                                .collect(Collectors.toList());
                List<ViewMetadata> views = entity.getViews().stream()
                                .map(TableMetadataMapper::toDomain)
                                .collect(Collectors.toList());
                return new FullTableMetadata(
                                entity.getId(),
                                entity.getDisplayName(),
                                entity.getPhysicalName(),
                                entity.getPhysicalDatabaseName(),
                                columns,
                                views);
        }

        public static BaseTableMetadata toDomainWithoutCollections(TableMetadataEntity entity) {
                return new BaseTableMetadata(
                                entity.getId(),
                                entity.getDisplayName(),
                                entity.getPhysicalName(),
                                entity.getPhysicalDatabaseName()
                );
        }

        public static ColumnMetadata toDomain(ColumnMetadataEntity entity) {
                if (!entity.getParent().isBlank()) {
                        return new ColumnMetadata(
                                entity.getId(),
                                entity.getDisplayName(),
                                entity.getPhysicalName(),
                                entity.getType(),
                                deserializeObject(entity.getParent(), ParentIdentifier.class)
                        );
                }
                return new ColumnMetadata(
                                entity.getId(),
                                entity.getDisplayName(),
                                entity.getPhysicalName(),
                                entity.getType());
        }

        public static ViewMetadata toDomain(ViewMetadataEntity entity) {
                PhysicalQuery query = deserializeObject(entity.getQuery(), PhysicalQuery.class);
                return new ViewMetadata(entity.getId(), entity.getName(), query);
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
                String foreignKey = "";
                if (dto.parent().isPresent()) {
                        foreignKey = serializeObject(dto.parent().get());
                }
                return new ColumnMetadataEntity(null, dto.displayName(), dto.physicalName(), dto.type(), foreignKey);
        }

        public static TableMetadataEntity fromDomain(FullTableMetadata domain) {
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
                String parent = "";
                if (domain.getParent().isPresent()) {
                        parent = serializeObject(domain.getParent().get());
                }
                return new ColumnMetadataEntity(
                        domain.getId(),
                        domain.getDisplayName(),
                        domain.getPhysicalName(),
                        domain.getType(),
                        parent
                );
        }

        public static ViewMetadataEntity fromDomain(ViewMetadata domain) {
                String queryJson = serializeObject(domain.query());
                return new ViewMetadataEntity(domain.id(), domain.name(), queryJson);
        }

        public static ViewMetadataEntity fromCreateDto(CreateTableViewDto domain) {
                String queryJson = serializeObject(domain.query());
                return new ViewMetadataEntity(null, domain.name(), queryJson);
        }

        public static String serializeObject(Object object) {
                try {
                        return objectMapper.writeValueAsString(object);
                } catch (JsonProcessingException e) {
                        throw new InternalServerError("Cannot serialize object", e);
                }
        }

        public static <T> T deserializeObject(String str, Class<T> clazz) {
                try {
                        return objectMapper.readValue(str, clazz);
                } catch (JsonProcessingException e) {
                        throw new InternalServerError("Cannot deserialize object", e);
                }
        }
}