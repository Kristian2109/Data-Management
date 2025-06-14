package com.kris.data_management.repositories;

import com.kris.data_management.dto.CreateTableDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.stream.Collectors;

@Repository
public class TableRepository {

    private final JdbcTemplate jdbcTemplate;

    public TableRepository(@Qualifier("dynamicJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void createTable(CreateTableDto createTableDto) {
        String sql = buildCreateTableSql(createTableDto);
        jdbcTemplate.execute(sql);
    }

    private String buildCreateTableSql(CreateTableDto createTableDto) {
        if (!isValidIdentifier(createTableDto.getTableName())) {
            throw new IllegalArgumentException("Invalid table name. Use only letters, numbers, and underscores.");
        }

        String columns = createTableDto.getColumns().stream()
            .map(col -> {
                if (!isValidIdentifier(col.getName()) || !isValidDataType(col.getType())) {
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
        return name != null && name.matches("[a-zA-Z0-9_]+");
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
} 