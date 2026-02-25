package com.interview.security;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;

/**
 * Enables WebFlux Security only when security.enabled=true
 * This is separated from SecurityConfig to avoid early bean initialization issues
 */
@Configuration
@ConditionalOnProperty(name = "security.enabled", havingValue = "true")
@EnableWebFluxSecurity
public class WebFluxSecurityEnabledConfig {
    // This class only serves to enable @EnableWebFluxSecurity when needed
}

