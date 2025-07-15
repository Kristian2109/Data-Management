package com.kris.data_management.middleware;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.MDC;

import java.io.IOException;
import java.util.UUID;

public class CorrelationIdFilter implements Filter {
    private static final String CORRELATION_ID_HEADER = "correlationId";

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
        throws IOException, ServletException {
        try {
            String correlationId = ((HttpServletRequest) servletRequest).getHeader(CORRELATION_ID_HEADER);

            if (correlationId == null || correlationId.isBlank()) {
                correlationId = UUID.randomUUID().toString();
            }

            MDC.put(CORRELATION_ID_HEADER, correlationId);

            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            MDC.remove(CORRELATION_ID_HEADER);
        }
    }
}
