package com.kris.data_management.mappers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kris.data_management.logical.table.BaseTableMetadata;
import com.kris.data_management.logical.table.CreateTableViewDto;
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
import com.kris.data_management.physical.dto.query.PhysicalQuery;

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
                return new ColumnMetadata(
                                entity.getId(),
                                entity.getDisplayName(),
                                entity.getPhysicalName(),
                                entity.getType());
        }

        public static ViewMetadata toDomain(ViewMetadataEntity entity) {
                PhysicalQuery query = deserializeObject(entity.getQuery(), PhysicalQuery.class);
                Map<String, String> physicalToDisplayColumnNames = new HashMap<>();

                if (entity.getPhysicalToDisplayColumnNames() != null &&
                    !entity.getPhysicalToDisplayColumnNames().isBlank()) {
                        physicalToDisplayColumnNames = deserializeObject(entity.getPhysicalToDisplayColumnNames(), HashMap.class);
                }
                return new ViewMetadata(entity.getId(), entity.getName(), query, physicalToDisplayColumnNames);
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
                                new ArrayList<>()
                );
        }

        public static ColumnMetadataEntity fromCreateDto(CreateColumnMetadataDto dto) {
                return new ColumnMetadataEntity(null, dto.displayName(), dto.physicalName(), dto.type());
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
                return new ColumnMetadataEntity(
                        domain.getId(),
                        domain.getDisplayName(),
                        domain.getPhysicalName(),
                        domain.getType()
                );
        }

        public static ViewMetadataEntity fromDomain(ViewMetadata domain) {
                String queryJson = serializeObject(domain.query());
                String columnNamesMapping = serializeObject(domain.physicalToDisplayColumnNames());
                return new ViewMetadataEntity(domain.id(), domain.name(), queryJson, columnNamesMapping);
        }

        public static ViewMetadataEntity fromCreateDto(CreateTableViewDto domain) {
                String queryJson = serializeObject(domain.query());
                String columnNamesMapping = serializeObject(domain.physicalToDisplayColumnNames());
                return new ViewMetadataEntity(null, domain.name(), queryJson, columnNamesMapping);
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