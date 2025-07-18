package com.kris.data_management.middleware;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class DatabaseFilterConfiguration {
    @Bean
    public FilterRegistrationBean<DatabaseFilter> databaseFilterFilterRegistrationBean(){
        FilterRegistrationBean<DatabaseFilter> registrationBean = new FilterRegistrationBean<>();

        registrationBean.setFilter(new DatabaseFilter());
        registrationBean.addUrlPatterns("/tables", "/relationship");
        registrationBean.setOrder(1);

        return registrationBean;
    }
}
