package com.kris.data_management.database;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DatabaseRoutingDataSource extends AbstractRoutingDataSource {

    private final ObjectProvider<DatabaseManagementService> databaseManagementServiceProvider;
    private final Map<Object, Object> targetDataSources = new ConcurrentHashMap<>();

    public DatabaseRoutingDataSource(ObjectProvider<DatabaseManagementService> databaseManagementServiceProvider) {
        this.databaseManagementServiceProvider = databaseManagementServiceProvider;
        setTargetDataSources(targetDataSources);
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return DatabaseContext.getCurrentDatabase();
    }

    @Override
    protected DataSource determineTargetDataSource() {
        String dbName = (String) determineCurrentLookupKey();
        if (dbName == null) {
            return super.determineTargetDataSource();
        }

        return (DataSource) targetDataSources.computeIfAbsent(dbName, this::buildDataSource);
    }

    private DataSource buildDataSource(Object dbName) {
        DatabaseManagementService service = databaseManagementServiceProvider.getIfAvailable();
        if (service != null) {
            return service.buildDataSourceForDatabase((String) dbName);
        }
        throw new IllegalStateException("DatabaseManagementService is not available to create a new DataSource.");
    }
} 