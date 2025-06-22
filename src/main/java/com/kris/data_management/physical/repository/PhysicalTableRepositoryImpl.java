package com.kris.data_management.physical.repository;

import com.kris.data_management.common.ColumnTypeMapper;
import com.kris.data_management.common.CreateColumnDto;
import com.kris.data_management.common.CreateTableDto;
import com.kris.data_management.logical.table.ColumnMetadata;
import com.kris.data_management.logical.table.CreateColumnMetadataDto;
import com.kris.data_management.physical.dto.CreatePhysicalColumnDto;
import com.kris.data_management.physical.dto.CreatePhysicalTableDto;
import com.kris.data_management.physical.dto.CreatePhysicalTableResult;
import com.kris.data_management.physical.exception.InvalidSqlIdentifierException;
import com.kris.data_management.physical.query.QueryResult;
import com.kris.data_management.utils.StorageUtils;
import com.kris.data_management.physical.query.PhysicalQuery;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
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

        validateCreateTable(dto);
        String columnsSql = dto.columns().stream()
                .map(col -> {
                    String name = createUniqueColumnName(col.displayName());
                    String type = ColumnTypeMapper.map(col.type()).getSqlType();
                    result.put(col.displayName(), name);
                    return "`" + name + "` " + type;
                })
                .collect(Collectors.joining(", "));

        String tableName = PhysicalTableRepositoryImpl.createUniqueTableName(dto.displayName());
        String sql = "CREATE TABLE `" + tableName + "` (id BIGINT AUTO_INCREMENT PRIMARY KEY, " + columnsSql + ")";
        jdbcTemplate.execute(sql);

        return new CreatePhysicalTableResult(tableName, result);
    }

    @Override
    public String addColumn(String tableName, CreateColumnDto col) {
        validateSqlTerm(tableName);
        validateCreateColumn(col);
        String uniqueColumnName = createUniqueColumnName(col.displayName());
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
            String columnName  = entry.getKey();
            validateSqlTerm(columnName);
            columnNames.add(columnName);
            placeholders.add("?");
            values.add(entry.getValue());
        }

        String sql = "INSERT INTO " + tableName + " (" + columnNames + ") VALUES (" + placeholders + ")";
        jdbcTemplate.update(sql, values.toArray());
    }

    @Override
    public void addRecords(String tableName, List<Map<String, String>> records) {

    }

    @Override
    public QueryResult executeQuery(PhysicalQuery query) {
        return null;
    }

    private static boolean isValidIdentifier(String name) {
        return name.matches("^[a-zA-Z_][a-zA-Z0-9_]*$");
    }

    private static void validateCreateTable(CreateTableDto dto) {
        validateSqlTerm(dto.displayName());
        dto.columns().forEach(PhysicalTableRepositoryImpl::validateCreateColumn);
    }

    private static void validateCreateColumn(CreateColumnDto dto) {
        validateSqlTerm(dto.displayName());
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
}