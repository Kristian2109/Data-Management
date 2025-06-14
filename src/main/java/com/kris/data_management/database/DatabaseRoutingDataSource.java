package com.kris.data_management.database;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DatabaseRoutingDataSource extends AbstractRoutingDataSource {

    private final DataSourceProperties databaseDataSourceProperties;
    private final Map<Object, Object> targetDataSources = new ConcurrentHashMap<>();

    public DatabaseRoutingDataSource(DataSourceProperties databaseDataSourceProperties) {
        this.databaseDataSourceProperties = databaseDataSourceProperties;
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

    private DataSource buildDataSource(Object dbNameObject) {
        String dbName = (String) dbNameObject;
        String url = databaseDataSourceProperties.getUrl() + dbName + "?createDatabaseIfNotExist=true&useSSL=false";

        DataSourceProperties customDataSourceProperties = new DataSourceProperties();
        customDataSourceProperties.setUrl(url);
        customDataSourceProperties.setUsername(databaseDataSourceProperties.getUsername());
        customDataSourceProperties.setPassword(databaseDataSourceProperties.getPassword());
        customDataSourceProperties.setDriverClassName(databaseDataSourceProperties.getDriverClassName());

        return customDataSourceProperties.initializeDataSourceBuilder().build();
    }
} 