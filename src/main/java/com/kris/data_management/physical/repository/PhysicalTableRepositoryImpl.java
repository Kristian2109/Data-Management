package com.kris.data_management.physical.repository;

import com.kris.data_management.physical.dto.CreatePhysicalColumnDto;
import com.kris.data_management.physical.dto.CreatePhysicalTableDto;
import com.kris.data_management.physical.exception.InvalidSqlIdentifierException;
import com.kris.data_management.physical.query.QueryResult;
import com.kris.data_management.physical.query.PhysicalQuery;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class PhysicalTableRepositoryImpl implements PhysicalTableRepository {

    private final JdbcTemplate jdbcTemplate;

    public PhysicalTableRepositoryImpl(@Qualifier("dynamicJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void createTable(CreatePhysicalTableDto dto) {
        validateCreateTable(dto);
        String sql = buildCreateTableSql(dto);
        jdbcTemplate.execute(sql);
    }

    private String buildCreateTableSql(CreatePhysicalTableDto createTableDto) {
        String columns = createTableDto.columns().stream()
            .map(col -> "`" + col.name() + "` " + col.type().getSqlType())
            .collect(Collectors.joining(", "));

        return "CREATE TABLE `" + createTableDto.name() + "` (id BIGINT AUTO_INCREMENT PRIMARY KEY, " + columns + ")";
    }

    @Override
    public void addColumn(String tableName, CreatePhysicalColumnDto col) {
        validateCreateColumn(col);
        String sql = "ALTER TABLE " + tableName + "\n" +
            "ADD " + "`" + col.name() + "` " + col.type().getSqlType() + ";";

        jdbcTemplate.execute(sql);
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

    private static boolean isValidIdentifier(String name) {
        return name.matches("^[a-zA-Z_][a-zA-Z0-9_]*$");
    }

    private static void validateCreateTable(CreatePhysicalTableDto dto) {
        if (isValidIdentifier(dto.name())) {
            throw new InvalidSqlIdentifierException(dto.name());
        }
        dto.columns().forEach(PhysicalTableRepositoryImpl::validateCreateColumn);
    }

    private static void validateCreateColumn(CreatePhysicalColumnDto dto) {
        if (isValidIdentifier(dto.name())) {
            throw new InvalidSqlIdentifierException(dto.name());
        }
    }
}