package com.kris.data_management.config;

import com.kris.data_management.database.DatabaseDatasourceService;
import com.kris.data_management.database.DatabaseRoutingDataSource;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class DataSourceConfiguration {
    @Bean
    @ConfigurationProperties("spring.datasource.admin")
    public DataSourceProperties adminDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean("adminDataSource")
    public DataSource adminDataSource() {
        return adminDataSourceProperties().initializeDataSourceBuilder().build();
    }

    @Bean("adminJdbcTemplate")
    public JdbcTemplate adminJdbcTemplate(@Qualifier("adminDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
    
    @Bean("databaseDataSourceProperties")
    @ConfigurationProperties("spring.datasource.database")
    public DataSourceProperties databaseDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean("routingDataSource")
    @Primary
    public DataSource routingDataSource(@Qualifier("databaseDataSourceProperties") DataSourceProperties databaseDataSourceProperties,
                                        @Qualifier("adminDataSource") DataSource adminDataSource) {
        DatabaseRoutingDataSource routingDataSource = new DatabaseRoutingDataSource(databaseDataSourceProperties);
        routingDataSource.setTargetDataSources(new ConcurrentHashMap<>());
        routingDataSource.setDefaultTargetDataSource(adminDataSource);
        return routingDataSource;
    }

    @Bean("dynamicJdbcTemplate")
    public JdbcTemplate jdbcTemplate(@Qualifier("routingDataSource") DataSource routingDataSource) {
        return new JdbcTemplate(routingDataSource);
    }
} 