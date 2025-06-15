package com.kris.data_management.middleware;

import com.kris.data_management.database.DatabaseContext;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class DatabaseFilter extends OncePerRequestFilter {

    private static final String DEFAULT_DATABASE_NAME = "database_management";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String dbName = request.getHeader("X-Database-Name");
        try {
            if (dbName == null) {
                dbName = DEFAULT_DATABASE_NAME;
            } 
            else if (dbName.isBlank()) {
                throw new IllegalArgumentException("Invalid database Name");
            }

            DatabaseContext.setCurrentDatabase(dbName);
            filterChain.doFilter(request, response);
        } finally {
            DatabaseContext.clear();
        }
    }
} 