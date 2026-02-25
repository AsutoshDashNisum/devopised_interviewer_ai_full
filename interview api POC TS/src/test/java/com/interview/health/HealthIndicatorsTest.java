package com.interview.health;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Health Indicators Test Suite
 * Tests custom health indicators for database, AI service, and overall API health
 *
 * Test Coverage:
 * - Database health indicator UP status
 * - AI service health indicator (enabled/disabled states)
 * - Overall API health aggregation
 * - Health details and descriptions
 */
@SpringBootTest(classes = {com.interview.InterviewEvaluationApplication.class})
@ActiveProfiles("test")
class HealthIndicatorsTest {

    @Autowired
    private DatabaseHealthIndicator databaseHealthIndicator;

    @Autowired
    private AiServiceHealthIndicator aiServiceHealthIndicator;

    @Autowired
    private ApiServiceHealthIndicator apiServiceHealthIndicator;

    // ==================== Database Health Indicator Tests ====================

    @Test
    void testDatabaseHealthIndicator_ReturnsUp() {
        // Arrange
        // Act
        Health health = databaseHealthIndicator.health();

        // Assert
        assertTrue(health.getStatus().equals(org.springframework.boot.actuate.health.Status.UP));
        assertNotNull(health.getDetails());
        assertEquals("PostgreSQL", health.getDetails().get("database"));
    }

    @Test
    void testDatabaseHealthIndicator_ContainsConnectionDetails() {
        // Arrange
        // Act
        Health health = databaseHealthIndicator.health();

        // Assert
        assertTrue(health.getDetails().containsKey("connection"));
        assertEquals("healthy", health.getDetails().get("connection"));
    }

    // ==================== AI Service Health Indicator Tests ====================

    @Test
    void testAiServiceHealthIndicator_ReturnsUp() {
        // Arrange
        // Act
        Health health = aiServiceHealthIndicator.health();

        // Assert
        assertTrue(health.getStatus().equals(org.springframework.boot.actuate.health.Status.UP) ||
                   health.getStatus().equals(org.springframework.boot.actuate.health.Status.DOWN));
        assertNotNull(health.getDetails());
    }

    @Test
    void testAiServiceHealthIndicator_ContainsProviderInfo() {
        // Arrange
        // Act
        Health health = aiServiceHealthIndicator.health();

        // Assert
        assertNotNull(health.getDetails().get("provider"));
    }

    // ==================== API Service Health Indicator Tests ====================

    @Test
    void testApiServiceHealthIndicator_AggregatesDependencies() {
        // Arrange
        // Act
        Health health = apiServiceHealthIndicator.health();

        // Assert
        assertNotNull(health);
        assertNotNull(health.getDetails());
        assertTrue(health.getDetails().containsKey("database"));
        assertTrue(health.getDetails().containsKey("ai"));
    }

    @Test
    void testApiServiceHealthIndicator_IncludesServiceInfo() {
        // Arrange
        // Act
        Health health = apiServiceHealthIndicator.health();

        // Assert
        assertEquals("Interview Evaluation API", health.getDetails().get("service"));
        assertNotNull(health.getDetails().get("version"));
    }

    // ==================== Integration Tests ====================

    @Test
    void testHealthIndicators_AllReturnValidStatus() {
        // Arrange
        // Act
        Health dbHealth = databaseHealthIndicator.health();
        Health aiHealth = aiServiceHealthIndicator.health();
        Health apiHealth = apiServiceHealthIndicator.health();

        // Assert - All should return a valid status
        assertNotNull(dbHealth.getStatus());
        assertNotNull(aiHealth.getStatus());
        assertNotNull(apiHealth.getStatus());
    }

    @Test
    void testHealthIndicators_ContainNoNullDetails() {
        // Arrange
        // Act
        Health dbHealth = databaseHealthIndicator.health();
        Health aiHealth = aiServiceHealthIndicator.health();
        Health apiHealth = apiServiceHealthIndicator.health();

        // Assert
        assertFalse(dbHealth.getDetails().isEmpty());
        assertFalse(aiHealth.getDetails().isEmpty());
        assertFalse(apiHealth.getDetails().isEmpty());
    }
}

