package com.kris.data_management.services;

import com.kris.data_management.database.dto.CreateDatabaseDto;
import com.kris.data_management.database.entities.DatabaseMetadataEntity;
import com.kris.data_management.database.repository.DatabaseMetadataRepositoryJpa;
import com.kris.data_management.utils.StorageUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DatabaseService {

    private final DatabaseMetadataRepositoryJpa databaseRepository;
    private final JdbcTemplate adminJdbcTemplate;

    private static final String DB_PREFIX = "db";
    private static final Integer RANDOM_PART_SIZE = 5;

    public DatabaseService(
        DatabaseMetadataRepositoryJpa databaseRepository,
        @Qualifier("adminJdbcTemplate") JdbcTemplate adminJdbcTemplate
    ) {
        this.databaseRepository = databaseRepository;
        this.adminJdbcTemplate = adminJdbcTemplate;
    }

    @Transactional
    public DatabaseMetadataEntity createDatabase(CreateDatabaseDto db) {
        DatabaseMetadataEntity dbEntity = new DatabaseMetadataEntity();
        String physicalName = StorageUtils.createPhysicalName(DB_PREFIX, db.getDisplayName(), RANDOM_PART_SIZE);
        dbEntity.setPhysicalName(physicalName);
        dbEntity.setDisplayName(db.getDisplayName());
        DatabaseMetadataEntity savedEntity = databaseRepository.save(dbEntity);

        createDatabaseSchema(dbEntity.getPhysicalName());

        return savedEntity;
    }

    public void createDatabaseSchema(String schemaName) {
        adminJdbcTemplate.execute("CREATE SCHEMA IF NOT EXISTS `" + schemaName + "`");
        createMetadataTables(schemaName);
    }

    private void createMetadataTables(String schemaName) {
        adminJdbcTemplate.execute("CREATE TABLE IF NOT EXISTS `" + schemaName + "`.`table_metadata` (" +
                "id BIGINT PRIMARY KEY AUTO_INCREMENT, " +
                "display_name VARCHAR(255) NOT NULL, " +
                "physical_name VARCHAR(255) NOT NULL" +
                ")");

        adminJdbcTemplate.execute("CREATE TABLE IF NOT EXISTS `" + schemaName + "`.`column_metadata` (" +
                "id BIGINT PRIMARY KEY AUTO_INCREMENT, " +
                "display_name VARCHAR(255) NOT NULL, " +
                "physical_name VARCHAR(255) NOT NULL, " +
                "type VARCHAR(255) NOT NULL, " +
                "table_id BIGINT, " +
                "CONSTRAINT fk_column_table FOREIGN KEY (table_id) REFERENCES `" + schemaName + "`.`table_metadata`(id) ON DELETE CASCADE" +
                ")");

        adminJdbcTemplate.execute("CREATE TABLE IF NOT EXISTS `" + schemaName + "`.`view_metadata` (" +
                "id BIGINT PRIMARY KEY AUTO_INCREMENT, " +
                "table_id BIGINT NOT NULL, " +
                "name VARCHAR(255) NOT NULL, " +
                "query_content TEXT NOT NULL, " +
                "CONSTRAINT fk_view_table FOREIGN KEY (table_id) REFERENCES `" + schemaName + "`.`table_metadata`(id) ON DELETE CASCADE" +
                ")");
    }
} 