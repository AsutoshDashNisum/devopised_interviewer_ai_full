package com.interview.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

/**
 * Security Configuration for WebFlux
 *
 * Features:
 * - JWT token-based authentication
 * - Basic Auth support (for testing and API clients)
 * - Role-based access control (RBAC)
 * - Stateless security context (no session storage)
 * - Public endpoints: /health, /api/v1/auth/login, /api/v1/auth/test
 * - Protected endpoints: /api/v1/evaluate, /api/v1/evaluate/full, /actuator/*
 * - Actuator health endpoint is public but metrics/info are admin-only
 */
@Configuration
public class SecurityConfig {

    private final Optional<JwtTokenProvider> jwtTokenProvider;

    @Value("${security.enabled:false}")
    private boolean securityEnabled;

    public SecurityConfig(Optional<JwtTokenProvider> jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }



    /**
     * Validates Basic Auth credentials
     * In production, check against database/LDAP/OAuth provider
     *
     * Test credentials:
     * - admin / admin123 (ADMIN role)
     * - user / user123 (USER role)
     */
    private boolean isValidBasicAuth(String username, String password) {
        // Test credentials - DO NOT use in production
        if ("admin".equals(username) && "admin123".equals(password)) {
            return true;
        }
        if ("user".equals(username) && "user123".equals(password)) {
            return true;
        }
        return false;
    }

    /**
     * Password encoder bean
     * Used for encoding passwords in production scenarios
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

