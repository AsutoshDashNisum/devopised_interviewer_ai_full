package com.interview.health;

import com.interview.config.AppProperties;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

/**
 * Database Health Indicator
 * Checks if the database (or data persistence layer) is healthy
 *
 * In a real application, this would:
 * - Test database connectivity with a simple query
 * - Check connection pool status
 * - Verify critical tables exist
 */
@Component
public class DatabaseHealthIndicator implements HealthIndicator {

    @Override
    public Health health() {
        try {
            // Simulate database connection check
            // In production: execute a simple query like "SELECT 1"
            boolean isDatabaseConnected = checkDatabaseConnection();

            if (isDatabaseConnected) {
                return Health.up()
                        .withDetail("database", "PostgreSQL")
                        .withDetail("connection", "healthy")
                        .withDetail("responseTime", "< 100ms")
                        .build();
            } else {
                return Health.down()
                        .withDetail("database", "PostgreSQL")
                        .withDetail("connection", "failed")
                        .withDetail("reason", "Connection timeout")
                        .build();
            }
        } catch (Exception ex) {
            return Health.down()
                    .withDetail("error", ex.getMessage())
                    .build();
        }
    }

    /**
     * Simulates database connection check
     * In production, execute actual SQL query
     */
    private boolean checkDatabaseConnection() {
        // For this POC, always return true
        // In production:
        // try {
        //     Connection conn = dataSource.getConnection();
        //     conn.isValid(2);
        //     conn.close();
        //     return true;
        // } catch (SQLException ex) {
        //     return false;
        // }
        return true;
    }
}

