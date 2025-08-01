package com.kris.data_management.services;

import com.kris.data_management.database.dto.CreateDatabaseDto;
import com.kris.data_management.database.dto.DatabaseMetadata;
import com.kris.data_management.database.dto.UpdateDatabaseDto;
import com.kris.data_management.database.repository.DatabaseMetadataRepository;
import com.kris.data_management.utils.StorageUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DatabaseService {

    private final DatabaseMetadataRepository databaseRepository;
    private final JdbcTemplate adminJdbcTemplate;

    private static final String DB_PREFIX = "db";
    private static final Integer RANDOM_PART_SIZE = 5;

    public DatabaseService(
            DatabaseMetadataRepository databaseRepository,
            @Qualifier("adminJdbcTemplate") JdbcTemplate adminJdbcTemplate) {
        this.databaseRepository = databaseRepository;
        this.adminJdbcTemplate = adminJdbcTemplate;
    }

    @Transactional
    public DatabaseMetadata createDatabase(CreateDatabaseDto db) {
        String physicalName = StorageUtils.createPhysicalName(DB_PREFIX, db.getDisplayName(), RANDOM_PART_SIZE);
        createDatabaseSchema(physicalName);

        return databaseRepository.create(physicalName, db.getDisplayName());
    }

    @Transactional
    public DatabaseMetadata update(String id, UpdateDatabaseDto dto) {
        return databaseRepository.update(id, dto);
    }

    public void createDatabaseSchema(String schemaName) {
        adminJdbcTemplate.execute("CREATE SCHEMA IF NOT EXISTS `" + schemaName + "`");
        createMetadataTables(schemaName);
    }

    private void createMetadataTables(String schemaName) {
        adminJdbcTemplate.execute("CREATE TABLE IF NOT EXISTS `" + schemaName + "`.`table_metadata` (" +
                "id BIGINT PRIMARY KEY AUTO_INCREMENT, " +
                "display_name VARCHAR(255) NOT NULL, " +
                "physical_name VARCHAR(255) NOT NULL, " +
                "physical_database_name VARCHAR(255) NOT NULL" +
                ")");

        adminJdbcTemplate.execute("CREATE TABLE IF NOT EXISTS `" + schemaName + "`.`column_metadata` (" +
                "id BIGINT PRIMARY KEY AUTO_INCREMENT, " +
                "display_name VARCHAR(255) NOT NULL, " +
                "physical_name VARCHAR(255) NOT NULL, " +
                "type VARCHAR(255) NOT NULL, " +
                "table_id BIGINT, " +
                "CONSTRAINT fk_column_table FOREIGN KEY (table_id) REFERENCES `" + schemaName
                + "`.`table_metadata`(id) ON DELETE CASCADE" +
                ")");

        adminJdbcTemplate.execute("CREATE TABLE IF NOT EXISTS `" + schemaName + "`.`view_metadata` (" +
                "id BIGINT PRIMARY KEY AUTO_INCREMENT, " +
                "table_id BIGINT NOT NULL, " +
                "name VARCHAR(255) NOT NULL, " +
                "physical_to_display_column_names TEXT, " +
                "query_content TEXT NOT NULL, " +
                "CONSTRAINT fk_view_table FOREIGN KEY (table_id) REFERENCES `" + schemaName
                + "`.`table_metadata`(id) ON DELETE CASCADE" +
                ")");

        adminJdbcTemplate.execute(
            "CREATE TABLE IF NOT EXISTS `" + schemaName + "`.`relationship_metadata` (" +
                    "id BIGINT PRIMARY KEY AUTO_INCREMENT, " +
                    "name VARCHAR(255) NOT NULL, " +
                    "parent_column_id BIGINT NOT NULL, " +
                    "child_column_id BIGINT NOT NULL, " +
                    "CONSTRAINT fk_parent_column FOREIGN KEY (parent_column_id) REFERENCES `" + schemaName + "`.`column_metadata`(id), " +
                    "CONSTRAINT fk_child_column FOREIGN KEY (child_column_id) REFERENCES `" + schemaName + "`.`column_metadata`(id)" +
                    ")"
        );
    }

    public DatabaseMetadata getDatabase(Long id) {
        return databaseRepository.get(id);
    }
    public DatabaseMetadata getDatabase(String  id) {
        return databaseRepository.get(id);
    }

    public java.util.List<DatabaseMetadata> getAllDatabases() {
        return databaseRepository.getAll();
    }
}