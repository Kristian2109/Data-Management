package com.kris.data_management.database;

import com.kris.data_management.entities.DatabaseEntity;
import com.kris.data_management.repositories.DatabaseRepository;
import com.kris.data_management.utils.StringUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DatabaseService {

    private final DatabaseRepository databaseRepository;
    private final DatabaseManagementService databaseManagementService;

    public DatabaseService(DatabaseRepository databaseRepository, DatabaseManagementService databaseManagementService) {
        this.databaseRepository = databaseRepository;
        this.databaseManagementService = databaseManagementService;
    }

    @Transactional
    public DatabaseEntity createDatabase(String name) {
        DatabaseEntity dbEntity = new DatabaseEntity();
        String physicalName = createPhysicalName(name);
        dbEntity.setPhysicalName(physicalName);
        dbEntity.setDisplayName(name);
        DatabaseEntity savedEntity = databaseRepository.save(dbEntity);

        databaseManagementService.createDatabaseSchema(physicalName);

        return savedEntity;
    }

    private String createPhysicalName(String displayName) {
        int randomPartLength = 5;
        return "db_" + "_" +
            displayName.replace(' ', '_') +
            "_" +
            StringUtil.generateRandomString(randomPartLength);
    }
} 