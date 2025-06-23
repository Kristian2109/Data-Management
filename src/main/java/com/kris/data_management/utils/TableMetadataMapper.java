package com.kris.data_management.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.kris.data_management.database.DatabaseContext;
import com.kris.data_management.logical.entities.ColumnMetadataEntity;
import com.kris.data_management.logical.entities.TableMetadataEntity;
import com.kris.data_management.logical.entities.ViewMetadataEntity;
import com.kris.data_management.logical.query.Query;
import com.kris.data_management.logical.table.ColumnMetadata;
import com.kris.data_management.logical.table.CreateColumnMetadataDto;
import com.kris.data_management.logical.table.CreateTableMetadataDto;
import com.kris.data_management.logical.table.TableMetadata;
import com.kris.data_management.logical.table.ViewMetadata;
import com.kris.data_management.physical.query.Filter;
import com.kris.data_management.physical.query.OrderBy;
import com.kris.data_management.physical.query.PhysicalQuery;
import com.kris.data_management.physical.query.Select;

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
            views
        );
    }

    public static ColumnMetadata toDomain(ColumnMetadataEntity entity) {
        return new ColumnMetadata(
            entity.getId(),
            entity.getDisplayName(),
            entity.getPhysicalName(),
            entity.getType()
        );
    }

    public static ViewMetadata toDomain(ViewMetadataEntity entity) {
        return new ViewMetadata(
            entity.getId(),
            entity.getName(),
            null
        );
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

    /**
     * Maps a logical Query to a PhysicalQuery using the provided table metadata.
     * @param query the logical query
     * @param tables the list of TableMetadata (should include all tables referenced in the query)
     * @return the mapped PhysicalQuery
     */
    public static PhysicalQuery mapToPhysicalQuery(
            Query query,
            List<TableMetadata> tables) {

        Function<Long, TableMetadata> getTable =
            id -> tables.stream().filter(t -> t.getId().equals(id)).findFirst().orElseThrow();

        BiFunction<Long, Long, ColumnMetadata> getColumn =
            (tableId, columnId) -> getTable.apply(tableId).getColumns().stream().filter(c -> c.getId().equals(columnId)).findFirst().orElseThrow();


        List<Select> physicalSelects = query.select().stream().flatMap(sel -> {
                TableMetadata table = getTable.apply(sel.tableId());
                return sel.columnIds().stream().map(colId -> {
                    ColumnMetadata col = getColumn.apply(sel.tableId(), colId);
                    return new com.kris.data_management.physical.query.Select(col.getPhysicalName(), table.getPhysicalName());
                });
            }).toList();

        List<Filter> physicalFilters = query.filters().stream().map(f -> {
                TableMetadata table = getTable.apply(f.tableId());
                ColumnMetadata col = getColumn.apply(f.tableId(), f.columnId());
                return new com.kris.data_management.physical.query.Filter(
                    col.getPhysicalName(),
                    table.getPhysicalName(),
                    f.operator(),
                    f.value() == null ? null : f.value().toString()
                );
            }).toList();

        // Map orders
        List<OrderBy> physicalOrders = query.orders().stream().map(o -> {
                TableMetadata table = getTable.apply(o.tableId());
                ColumnMetadata col = getColumn.apply(o.tableId(), o.columnId());
                return new com.kris.data_management.physical.query.OrderBy(
                    col.getPhysicalName(),
                    table.getPhysicalName(),
                    o.direction()
                );
            }).toList();

        // Map joins
//        List<com.kris.data_management.physical.query.Join> physicalJoins = query.joins() == null ? List.of() :
//            query.joins().stream().map(j -> {
//                TableMetadata leftTable = getTable.apply(j.firstTableId());
//                TableMetadata rightTable = getTable.apply(j.secondTableId());
//                ColumnMetadata leftCol = getColumn.apply(j.firstTableId(), j.firstTableColumnId());
//                ColumnMetadata rightCol = getColumn.apply(j.secondTableId(), j.secondTableColumnId());
//                return new com.kris.data_management.physical.query.Join(
//                    leftTable.getPhysicalName(),
//                    leftCol.getPhysicalName(),
//                    rightTable.getPhysicalName(),
//                    rightCol.getPhysicalName()
//                );
//            }).toList();
//
//        String mainTableName = physicalSelects.isEmpty() ? null : physicalSelects.get(0).tableName();

        return new PhysicalQuery(
            physicalSelects,
            physicalFilters,
            physicalOrders,
            query.pagination(),
            new ArrayList<>(),
            getTable.apply(query.tableId()).getPhysicalName()
        );
    }
} 