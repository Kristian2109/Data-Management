package com.kris.data_management.database;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;

@Service
public class DatabaseManagementService {

    private final JdbcTemplate adminJdbcTemplate;
    private final DataSourceProperties dataSourceProperties;

    public DatabaseManagementService(@Qualifier("adminJdbcTemplate") JdbcTemplate adminJdbcTemplate,
                                   @Qualifier("databaseDataSourceProperties") DataSourceProperties dataSourceProperties) {
        this.adminJdbcTemplate = adminJdbcTemplate;
        this.dataSourceProperties = dataSourceProperties;
    }

    public void createDatabaseSchema(String schemaName) {
        adminJdbcTemplate.execute("CREATE SCHEMA IF NOT EXISTS `" + schemaName + "`");
    }

    public DataSource buildDataSourceForDatabase(String dbName) {
        String url = dataSourceProperties.getUrl();
        String connectionUrl = url + dbName + "?createDatabaseIfNotExist=true&useSSL=false";

        DataSourceProperties customDataSourceProperties = new DataSourceProperties();
        customDataSourceProperties.setUrl(connectionUrl);
        customDataSourceProperties.setUsername(dataSourceProperties.getUsername());
        customDataSourceProperties.setPassword(dataSourceProperties.getPassword());
        customDataSourceProperties.setDriverClassName(dataSourceProperties.getDriverClassName());

        return customDataSourceProperties.initializeDataSourceBuilder().build();
    }
} 