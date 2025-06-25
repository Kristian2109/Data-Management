package com.kris.data_management.physical.repository;

import com.kris.data_management.common.ColumnTypeMapper;
import com.kris.data_management.common.CreateColumnDto;
import com.kris.data_management.common.CreateTableDto;
import com.kris.data_management.common.FilterOperator;
import com.kris.data_management.physical.dto.ColumnValue;
import com.kris.data_management.physical.dto.Record;
import com.kris.data_management.physical.dto.CreatePhysicalTableResult;
import com.kris.data_management.physical.exception.InvalidSqlIdentifierException;
import com.kris.data_management.physical.query.Filter;
import com.kris.data_management.physical.query.Join;
import com.kris.data_management.physical.query.OrderBy;
import com.kris.data_management.physical.query.QueryResult;
import com.kris.data_management.physical.query.Select;
import com.kris.data_management.utils.StorageUtils;
import com.kris.data_management.physical.query.PhysicalQuery;
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

        String columnType = ColumnTypeMapper.map(col.type()).getSqlType();
        String sql = "ALTER TABLE " + tableName + "\n" +
                "ADD " + "`" + uniqueColumnName + "` " + columnType + ";";

        jdbcTemplate.execute(sql);
        return uniqueColumnName;
    }

    @Override
    public void addRecord(String tableName, Map<String, String> valuePerColumn) {
        validateSqlTerm(tableName);

        StringJoiner columnNames = new StringJoiner(", ");
        StringJoiner placeholders = new StringJoiner(", ");
        List<String> values = new ArrayList<>();

        for (Map.Entry<String, String> entry : valuePerColumn.entrySet()) {
            String columnName = entry.getKey();
            validateSqlTerm(columnName);
            columnNames.add(columnName);
            placeholders.add("?");
            values.add(entry.getValue());
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
        StringJoiner selectPart = new StringJoiner(", ");

        for (Select select : query.select()) {
            selectPart.add(select.tableName() + "." + select.columnName());
        }

        StringJoiner filters = new StringJoiner(" AND ");
        for (Filter filter : query.filters()) {
            String columnIdentifier = filter.tableName() + "." + filter.columnName();
            String filterOperator = toFilterMap(filter.operator());
            filters.add(columnIdentifier + " " + filterOperator + " " + filter.value());
        }

        StringJoiner orders = new StringJoiner(", ");
        for (OrderBy orderBy : query.orders()) {
            String columnIdentifier = orderBy.tableName() + "." + orderBy.columnName();
            orders.add(columnIdentifier + " " + orderBy.direction().toString());
        }

        long offset = query.pagination().pageNumber() * (query.pagination().pageSize() - 1);
        String pagination = "LIMIT " + query.pagination().pageSize() + " OFFSET " + offset;

        StringBuilder joinsSql = new StringBuilder();
        for (Join join : query.joins()) {
            // noinspection StringConcatenationInsideStringBufferAppend
            joinsSql
                    .append("LEFT JOIN ")
                    .append(join.rightTableName())
                    .append(" ON ")
                    .append(join.leftTableName() + "." + join.leftColumnName())
                    .append(" = ")
                    .append(join.rightTableName() + "." + join.rightColumnName())
                    .append(" ");

        }

        String sql = "SELECT " + selectPart +
                " FROM " + tableName +
                " " + joinsSql +
                " WHERE " + filters +
                " ORDER BY " + orders +
                " " + pagination;

        List<Map<String, Object>> objects = jdbcTemplate.queryForList(sql);

        List<Record> records = objects.stream().map(
                obj -> {
                    List<ColumnValue> values = obj.values()
                            .stream()
                            .map(v -> new ColumnValue(v.toString()))
                            .toList();
                    return new Record(values);
                }).toList();

        return new QueryResult(100L, records);
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