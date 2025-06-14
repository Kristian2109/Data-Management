package com.kris.data_management.database;

import com.kris.data_management.database.entities.DatabaseMetadataEntity;
import com.kris.data_management.repositories.DatabaseRepository;
import com.kris.data_management.utils.StringUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DatabaseService {

    private final DatabaseRepository databaseRepository;
    private final DatabaseDatasourceService databaseManagementService;

    public DatabaseService(DatabaseRepository databaseRepository, DatabaseDatasourceService databaseManagementService) {
        this.databaseRepository = databaseRepository;
        this.databaseManagementService = databaseManagementService;
    }

    @Transactional
    public DatabaseMetadataEntity createDatabase(String name) {
        DatabaseMetadataEntity dbEntity = new DatabaseMetadataEntity();
        String physicalName = createPhysicalName(name);
        dbEntity.setPhysicalName(physicalName);
        dbEntity.setDisplayName(name);
        DatabaseMetadataEntity savedEntity = databaseRepository.save(dbEntity);

        databaseManagementService.createDatabaseSchema(physicalName);

        return savedEntity;
    }

    private String createPhysicalName(String displayName) {
        int randomPartLength = 5;
        return "db_" +
            displayName.replace(' ', '_') +
            "_" +
            StringUtil.generateRandomString(randomPartLength);
    }
} 