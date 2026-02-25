package com.interview.security;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;

/**
 * Provides a permissive SecurityWebFilterChain when security is disabled
 */
@Configuration
@ConditionalOnProperty(name = "security.enabled", havingValue = "false", matchIfMissing = true)
@EnableWebFluxSecurity
public class WebFluxSecurityDisabledConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) throws Exception {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchanges -> exchanges.anyExchange().permitAll())
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
                .build();
    }
}

