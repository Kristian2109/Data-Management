package com.kris.data_management.repositories;

import com.kris.data_management.dtos.CreatePhysicalColumnDto;
import com.kris.data_management.dtos.CreateTableDto;
import com.kris.data_management.dtos.model.logicalQuery.QueryResult;
import com.kris.data_management.dtos.model.physicalQuery.PhysicalQuery;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class TableRepository implements PhysicalTableRepository {

    private final JdbcTemplate jdbcTemplate;

    public TableRepository(@Qualifier("dynamicJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void createTable(CreateTableDto createTableDto) {
        String sql = buildCreateTableSql(createTableDto);
        jdbcTemplate.execute(sql);
    }

    private String buildCreateTableSql(CreateTableDto createTableDto) {
        if (isValidIdentifier(createTableDto.getTableName())) {
            throw new IllegalArgumentException("Invalid table name. Use only letters, numbers, and underscores.");
        }

        String columns = createTableDto.getColumns().stream()
            .map(col -> {
                if (isValidIdentifier(col.getName()) || !isValidDataType(col.getType())) {
                    throw new IllegalArgumentException("Invalid column name or data type for column: " + col.getName());
                }
                return "`" + col.getName() + "` " + col.getType();
            })
            .collect(Collectors.joining(", "));

        if (columns.isEmpty()) {
            throw new IllegalArgumentException("Table must have at least one column.");
        }

        return "CREATE TABLE `" + createTableDto.getTableName() + "` (id BIGINT AUTO_INCREMENT PRIMARY KEY, " + columns + ")";
    }

    private boolean isValidIdentifier(String name) {
        return name == null || !name.matches("[a-zA-Z0-9_]+");
    }

    private boolean isValidDataType(String type) {
        String upperType = type.toUpperCase();
        // Basic validation for common types
        return upperType.startsWith("VARCHAR") || upperType.startsWith("INT") ||
            upperType.startsWith("BIGINT") || upperType.startsWith("TEXT") ||
            upperType.startsWith("DATE") || upperType.startsWith("TIMESTAMP") ||
            upperType.startsWith("BOOLEAN") || upperType.startsWith("DECIMAL") ||
            upperType.startsWith("DOUBLE") || upperType.startsWith("FLOAT");
    }

    @Override
    public void createTable(String tableName, List<CreatePhysicalColumnDto> columns) {

    }

    @Override
    public void addColumn(String tableName, CreatePhysicalColumnDto column) {

    }

    @Override
    public void addRecord(String tableName, Map<String, String> valuePerColumn) {

    }

    @Override
    public void addRecords(String tableName, List<Map<String, String>> records) {

    }

    @Override
    public QueryResult executeQuery(PhysicalQuery query) {
        return null;
    }
}