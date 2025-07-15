package com.kris.data_management.middleware;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.logstash.logback.argument.StructuredArguments;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Order(2)
@Component
public class RequestLoggingFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(RequestLoggingFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {

        logger.info("Incoming request!",
            StructuredArguments.keyValue("event", "incoming-request"),
            StructuredArguments.keyValue("httpMethod", request.getMethod()),
            StructuredArguments.keyValue("uri", request.getRequestURI()),
            StructuredArguments.keyValue("remoteIp", request.getRemoteAddr())
        );

        filterChain.doFilter(request, response);
    }
}