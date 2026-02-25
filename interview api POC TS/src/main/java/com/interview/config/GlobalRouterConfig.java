package com.interview.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/**
 * Global WebFlux routes configuration
 * Defines health check and root endpoint
 */
@Configuration
public class GlobalRouterConfig {

    /**
     * CORS Web Filter for WebFlux
     * Applies CORS configuration to all routes
     */
    @Bean
    public CorsWebFilter corsWebFilter(CorsConfigurationSource corsConfigurationSource) {
        return new CorsWebFilter(corsConfigurationSource);
    }

    /**
     * Health check and root endpoints
     */
    @Bean
    public RouterFunction<ServerResponse> globalRoutes() {
        return route(GET("/health"), request ->
                ServerResponse.ok().bodyValue(new HealthResponse("ok")))
                .andRoute(GET("/"), request ->
                        ServerResponse.ok().bodyValue(new ApiInfoResponse()));
    }

    /**
     * Health check response DTO
     */
    public static class HealthResponse {
        private String status;
        private String timestamp;
        private String environment;

        public HealthResponse(String status) {
            this.status = status;
            this.timestamp = java.time.Instant.now().toString();
            this.environment = System.getenv().getOrDefault("APP_ENVIRONMENT", "development");
        }

        public String getStatus() {
            return status;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public String getEnvironment() {
            return environment;
        }
    }

    /**
     * API info response DTO
     */
    public static class ApiInfoResponse {
        private String service;
        private String version;
        private EndpointsInfo endpoints;

        public ApiInfoResponse() {
            this.service = "Interview Evaluation API";
            this.version = "v1";
            this.endpoints = new EndpointsInfo();
        }

        public String getService() {
            return service;
        }

        public String getVersion() {
            return version;
        }

        public EndpointsInfo getEndpoints() {
            return endpoints;
        }

        public static class EndpointsInfo {
            private String evaluate;
            private String health;

            public EndpointsInfo() {
                this.evaluate = "POST /api/v1/evaluate";
                this.health = "GET /health";
            }

            public String getEvaluate() {
                return evaluate;
            }

            public String getHealth() {
                return health;
            }
        }
    }

}
