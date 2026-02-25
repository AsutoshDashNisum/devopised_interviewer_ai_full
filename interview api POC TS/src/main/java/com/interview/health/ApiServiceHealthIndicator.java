package com.interview.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

/**
 * API Service Health Indicator
 * Checks overall API health status including critical components
 */
@Component
public class ApiServiceHealthIndicator implements HealthIndicator {

    private final DatabaseHealthIndicator databaseHealthIndicator;
    private final AiServiceHealthIndicator aiServiceHealthIndicator;

    public ApiServiceHealthIndicator(
            DatabaseHealthIndicator databaseHealthIndicator,
            AiServiceHealthIndicator aiServiceHealthIndicator) {
        this.databaseHealthIndicator = databaseHealthIndicator;
        this.aiServiceHealthIndicator = aiServiceHealthIndicator;
    }

    @Override
    public Health health() {
        try {
            Health dbHealth = databaseHealthIndicator.health();
            Health aiHealth = aiServiceHealthIndicator.health();

            // Overall API is UP if database is UP
            if (dbHealth.getStatus().equals(org.springframework.boot.actuate.health.Status.UP)) {
                return Health.up()
                        .withDetail("service", "Interview Evaluation API")
                        .withDetail("version", "0.1.0")
                        .withDetail("database", dbHealth.getDetails())
                        .withDetail("ai", aiHealth.getDetails())
                        .build();
            } else {
                return Health.down()
                        .withDetail("service", "Interview Evaluation API")
                        .withDetail("reason", "Critical service dependency down")
                        .withDetail("database", dbHealth.getDetails())
                        .build();
            }
        } catch (Exception ex) {
            return Health.down()
                    .withDetail("error", ex.getMessage())
                    .build();
        }
    }
}

