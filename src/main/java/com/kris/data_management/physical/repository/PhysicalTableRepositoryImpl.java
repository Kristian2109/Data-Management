package com.kris.data_management.physical.repository;

import com.kris.data_management.common.*;
import com.kris.data_management.physical.dto.ColumnValue;
import com.kris.data_management.physical.dto.DatabaseColumnType;
import com.kris.data_management.physical.dto.Record;
import com.kris.data_management.physical.dto.CreatePhysicalTableResult;
import com.kris.data_management.physical.exception.InvalidSqlIdentifierException;
import com.kris.data_management.physical.query.Filter;
import com.kris.data_management.physical.query.PhysicalQuery;
import com.kris.data_management.physical.query.QueryResult;
import com.kris.data_management.utils.StorageUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Collectors;

@Repository
public class PhysicalTableRepositoryImpl implements PhysicalTableRepository {
    private static final String TABLE_PREFIX = "tb";
    private static final String COLUMN_PREFIX = "col";
    private static final Integer RANDOM_PART_SIZE = 5;

    private final JdbcTemplate jdbcTemplate;

    public PhysicalTableRepositoryImpl(@Qualifier("dynamicJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public CreatePhysicalTableResult createTable(CreateTableDto dto) {
        Map<String, String> result = new HashMap<>();
        result.put("id", "id");

        String columnsSql = dto.columns().stream()
                .map(col -> {
                    String name = createUniqueColumnName(col.displayName());
                    validateSqlTerm(name);
                    String type = ColumnTypeMapper.map(col.type()).getSqlType();
                    result.put(col.displayName(), name);
                    return "`" + name + "` " + type;
                })
                .collect(Collectors.joining(", "));

        String tableName = PhysicalTableRepositoryImpl.createUniqueTableName(dto.displayName());
        validateSqlTerm(tableName);
        String sql = "CREATE TABLE `" + tableName + "` (id BIGINT AUTO_INCREMENT PRIMARY KEY, " + columnsSql + ")";
        jdbcTemplate.execute(sql);

        return new CreatePhysicalTableResult(tableName, result);
    }

    @Override
    public String addColumn(String tableName, CreateColumnDto col) {
        validateSqlTerm(tableName);

        String uniqueColumnName = createUniqueColumnName(col.displayName());
        validateSqlTerm(uniqueColumnName);

        DatabaseColumnType columnType = ColumnTypeMapper.map(col.type());

        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder
                .append("ALTER TABLE ")
                .append(tableName)
                .append(" ADD COLUMN ")
                .append(uniqueColumnName)
                .append(" ")
                .append(columnType.getSqlType());

        if (columnType.equals(DatabaseColumnType.FOREIGN_KEY)) {
            ParentIdentifier parent = col.parent()
                    .orElseThrow(() -> new IllegalArgumentException("parent table is required"));

            queryBuilder
                    .append(", ADD FOREIGN KEY (")
                    .append(uniqueColumnName)
                    .append(") REFERENCES ")
                    .append(parent.table())
                    .append("(")
                    .append(parent.column())
                    .append(")");
        }

        jdbcTemplate.execute(queryBuilder.toString());
        return uniqueColumnName;
    }

    @Override
    public void addRecord(String tableName, CreateRecordDto recordDto) {
        validateSqlTerm(tableName);

        StringJoiner columnNames = new StringJoiner(", ");
        StringJoiner placeholders = new StringJoiner(", ");
        List<String> values = new ArrayList<>();

        for (RecordColumnValue columnValue : recordDto.columnValues()) {
            validateSqlTerm(columnValue.columnName());
            columnNames.add(columnValue.columnName());
            placeholders.add("?");
            values.add(columnValue.stringValue());
        }

        String sql = "INSERT INTO " + tableName + " (" + columnNames + ") VALUES (" + placeholders + ")";
        jdbcTemplate.update(sql, values.toArray());
    }

    @Override
    public void addRecords(String tableName, List<String> columnNames, List<List<String>> records) {
        validateSqlTerm(tableName);
        columnNames.forEach(PhysicalTableRepositoryImpl::validateSqlTerm);

        StringJoiner recordsSql = new StringJoiner(", ");
        List<String> allValues = new ArrayList<>();
        for (List<String> record : records) {
            StringJoiner placeholders = new StringJoiner(", ");
            record.forEach(v -> placeholders.add("?"));
            allValues.addAll(record);
            recordsSql.add("( " + placeholders + " )");
        }

        String columnNamesSql = String.join(", ", columnNames);
        String sql = "INSERT INTO " + tableName + " (" + columnNamesSql + ") VALUES " + recordsSql;
        jdbcTemplate.update(sql, allValues.toArray());
    }

    @Override
    public QueryResult executeQuery(String tableName, PhysicalQuery query) {
        validateQuery(query);
        List<Object> queryParams = new ArrayList<>();

        String fromClause = "FROM " + tableName;
        String joinClause = buildJoinClause(query);
        String whereClause = buildWhereClause(query, queryParams);

        long totalRecords = countTotalRecords(fromClause, joinClause, whereClause, queryParams);

        String selectClause = buildSelectClause(query);
        String orderByClause = buildOrderByClause(query);
        String paginationClause = buildPaginationClause(query, queryParams);

        String querySql = String.join(" ",
                selectClause,
                fromClause,
                joinClause,
                whereClause,
                orderByClause,
                paginationClause
        ).trim().replaceAll(" +", " ");

        List<Map<String, Object>> objects = jdbcTemplate.queryForList(querySql, queryParams.toArray());
        List<Record> records = mapResultsToRecords(objects);

        return new QueryResult(totalRecords, records);
    }

    private void validateQuery(PhysicalQuery query) {
        query.select().forEach(s -> {
            validateSqlTerm(s.tableName());
            validateSqlTerm(s.columnName());
        });

        query.filters().forEach(f -> {
            validateSqlTerm(f.tableName());
            validateSqlTerm(f.columnName());
        });

        query.orders().forEach(o -> {
            validateSqlTerm(o.tableName());
            validateSqlTerm(o.columnName());
        });

        query.joins().forEach(j -> {
            validateSqlTerm(j.rightTableName());
            validateSqlTerm(j.rightColumnName());
            validateSqlTerm(j.leftTableName());
            validateSqlTerm(j.leftColumnName());
        });
    }

    private List<Record> mapResultsToRecords(List<Map<String, Object>> objects) {
        return objects.stream().map(
                obj -> {
                    List<ColumnValue> values = obj.values()
                            .stream()
                            .map(v -> new ColumnValue(v != null ? v.toString() : null))
                            .toList();
                    return new Record(values);
                }).toList();
    }

    private long countTotalRecords(String fromClause, String joinClause, String whereClause, List<Object> params) {
        String countQuery = String.join(" ",
                "SELECT COUNT(*)",
                fromClause,
                joinClause,
                whereClause
        ).trim().replaceAll(" +", " ");

        return jdbcTemplate.queryForObject(countQuery, Long.class, params.toArray());
    }

    private String buildSelectClause(PhysicalQuery query) {
        StringJoiner selectPart = new StringJoiner(", ");
        query.select().forEach(select -> selectPart.add(select.tableName() + "." + select.columnName()));
        return "SELECT " + selectPart;
    }

    private String buildJoinClause(PhysicalQuery query) {
        if (query.joins().isEmpty()) {
            return "";
        }

        StringBuilder joinsSql = new StringBuilder();
        query.joins().forEach(join -> {
            joinsSql.append("LEFT JOIN ")
                    .append(join.rightTableName())
                    .append(" ON ")
                    .append(join.leftTableName()).append(".").append(join.leftColumnName())
                    .append(" = ")
                    .append(join.rightTableName()).append(".").append(join.rightColumnName())
                    .append(" ");
        });

        return joinsSql.toString();
    }

    private String buildWhereClause(PhysicalQuery query, List<Object> params) {
        if (query.filters().isEmpty()) {
            return "";
        }

        StringJoiner filters = new StringJoiner(" AND ");
        query.filters().stream().forEach(filter -> {
            String columnIdentifier = filter.tableName() + "." + filter.columnName();
            String filterOperator = toFilterMap(filter.operator());
            filters.add(columnIdentifier + " " + filterOperator + " ?");
            params.add(filter.value());
        });

        return "WHERE " + filters;
    }

    private String buildOrderByClause(PhysicalQuery query) {
        if (query.orders().isEmpty()) {
            return "";
        }
        StringJoiner orders = new StringJoiner(", ");
        query.orders().forEach(orderBy -> {
            String columnIdentifier = orderBy.tableName() + "." + orderBy.columnName();
            orders.add(columnIdentifier + " " + orderBy.direction().toString());
        });
        return "ORDER BY " + orders;
    }

    private String buildPaginationClause(PhysicalQuery query, List<Object> params) {
        Pagination pagination = query.pagination();
        long offset = (long) pagination.pageNumber() * pagination.pageSize();
        params.add(pagination.pageSize());
        params.add(offset);
        return "LIMIT ? OFFSET ?";
    }

    private static boolean isValidIdentifier(String name) {
        return name.matches("^[a-zA-Z_][a-zA-Z0-9_]*$");
    }

    private static void validateSqlTerm(String term) {
        if (!isValidIdentifier(term)) {
            throw new InvalidSqlIdentifierException(term);
        }
    }

    private static String createUniqueColumnName(String displayName) {
        return StorageUtils.createPhysicalName(COLUMN_PREFIX, displayName, RANDOM_PART_SIZE);
    }

    private static String createUniqueTableName(String displayName) {
        return StorageUtils.createPhysicalName(TABLE_PREFIX, displayName, RANDOM_PART_SIZE);
    }

    private static String toFilterMap(FilterOperator operator) {
        switch (operator) {
            case LESS -> {
                return "<";
            }
            case GREATER -> {
                return ">";
            }
            case NOT_EQUAL -> {
                return "!=";
            }
            default -> {
                return "=";
            }
        }
    }
}