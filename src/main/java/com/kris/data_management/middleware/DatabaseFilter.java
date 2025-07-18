package com.kris.data_management.middleware;

import com.kris.data_management.database.DatabaseContext;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class DatabaseFilter extends OncePerRequestFilter {
    private static final String DATABASE_HEADER = "X-Database-Name";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String dbName = request.getHeader(DATABASE_HEADER);
            if (dbName == null || dbName.isBlank()) {
                throw new IllegalArgumentException("Invalid database Name");
            }

            DatabaseContext.setCurrentDatabase(dbName);
            MDC.put(DATABASE_HEADER, dbName);
            filterChain.doFilter(request, response);

        } finally {
            DatabaseContext.clear();
            MDC.remove(DATABASE_HEADER);
        }
    }
} 