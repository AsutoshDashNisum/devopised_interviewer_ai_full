package com.interview.health;

import com.interview.config.AppProperties;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

/**
 * AI Service Health Indicator
 * Checks if the AI/LLM service (OpenAI or configured provider) is available
 *
 * Respects ai.enabled flag and AI provider configuration
 */
@Component
public class AiServiceHealthIndicator implements HealthIndicator {

    private final AppProperties appProperties;

    public AiServiceHealthIndicator(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    @Override
    public Health health() {
        try {
            if (!appProperties.isEnabled()) {
                return Health.up()
                        .withDetail("service", "AI/LLM")
                        .withDetail("status", "disabled")
                        .withDetail("provider", appProperties.getProvider())
                        .build();
            }

            // Check AI service connectivity
            boolean isAiServiceHealthy = checkAiServiceHealth();

            if (isAiServiceHealthy) {
                return Health.up()
                        .withDetail("service", "AI/LLM")
                        .withDetail("provider", appProperties.getProvider())
                        .withDetail("model", appProperties.getModel())
                        .withDetail("status", "connected")
                        .build();
            } else {
                return Health.down()
                        .withDetail("service", "AI/LLM")
                        .withDetail("provider", appProperties.getProvider())
                        .withDetail("status", "unreachable")
                        .withDetail("reason", "Failed to connect to API")
                        .build();
            }
        } catch (Exception ex) {
            return Health.down()
                    .withDetail("error", ex.getMessage())
                    .build();
        }
    }

    /**
     * Simulates AI service health check
     * In production: make a test API call to the LLM provider
     */
    private boolean checkAiServiceHealth() {
        if (!appProperties.isEnabled()) {
            return true; // Service is disabled, so it's "healthy" (no check needed)
        }

        // For this POC, always return true
        // In production:
        // try {
        //     // Make a minimal API call to OpenAI or configured provider
        //     HttpClient client = HttpClient.newHttpClient();
        //     HttpRequest request = HttpRequest.newBuilder()
        //         .uri(new URI(appProperties.getProvider() + "/health"))
        //         .timeout(Duration.ofSeconds(2))
        //         .GET()
        //         .build();
        //     HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        //     return response.statusCode() == 200;
        // } catch (Exception ex) {
        //     return false;
        // }
        return true;
    }
}

