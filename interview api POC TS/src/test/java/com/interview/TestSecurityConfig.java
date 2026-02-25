package com.interview;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;

/**
 * Test-specific security configuration
 * Provides a permissive security chain for testing
 * Works with WebFluxSecurityDisabledConfig which has @EnableWebFluxSecurity
 */
@TestConfiguration
public class TestSecurityConfig {

    @Bean
    @Primary
    public SecurityWebFilterChain testSecurityWebFilterChain(ServerHttpSecurity http) throws Exception {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchanges -> exchanges.anyExchange().permitAll())
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
                .build();
    }
}


