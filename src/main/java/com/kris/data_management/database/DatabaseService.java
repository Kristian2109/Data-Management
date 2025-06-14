package com.kris.data_management.database;

import com.kris.data_management.database.entities.DatabaseMetadataEntity;
import com.kris.data_management.repositories.DatabaseMetadataRepository;
import com.kris.data_management.utils.StringUtil;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DatabaseService {

    private final DatabaseMetadataRepository databaseRepository;
    private final JdbcTemplate adminJdbcTemplate;

    public DatabaseService(
        DatabaseMetadataRepository databaseRepository,
        @Qualifier("adminJdbcTemplate") JdbcTemplate adminJdbcTemplate
    ) {
        this.databaseRepository = databaseRepository;
        this.adminJdbcTemplate = adminJdbcTemplate;
    }

    @Transactional
    public DatabaseMetadataEntity createDatabase(String name) {
        DatabaseMetadataEntity dbEntity = new DatabaseMetadataEntity();
        String physicalName = createPhysicalName(name);
        dbEntity.setPhysicalName(physicalName);
        dbEntity.setDisplayName(name);
        DatabaseMetadataEntity savedEntity = databaseRepository.save(dbEntity);

        createDatabaseSchema(physicalName);

        return savedEntity;
    }

    private String createPhysicalName(String displayName) {
        int randomPartLength = 5;
        return "db_" +
            displayName.replace(' ', '_') +
            "_" +
            StringUtil.generateRandomString(randomPartLength);
    }

    public void createDatabaseSchema(String schemaName) {
        adminJdbcTemplate.execute("CREATE SCHEMA IF NOT EXISTS `" + schemaName + "`");
    }
} 