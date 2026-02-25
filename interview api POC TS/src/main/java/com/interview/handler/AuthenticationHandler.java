package com.interview.handler;

import com.interview.dto.LoginRequest;
import com.interview.dto.LoginResponse;
import com.interview.security.JwtTokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Authentication Handler
 * Handles login requests and generates JWT tokens
 *
 * Endpoints:
 * - POST /api/v1/auth/login - User login (generates JWT token)
 * - POST /api/v1/auth/test - Test authentication (returns test token)
 */
@Component
public class AuthenticationHandler {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationHandler.class);
    private final JwtTokenProvider jwtTokenProvider;

    public AuthenticationHandler(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /**
     * Handles POST /api/v1/auth/login requests
     * Validates credentials and returns JWT token
     *
     * Test credentials:
     * - admin / admin123 (ADMIN role)
     * - user / user123 (USER role)
     */
    public Mono<ServerResponse> login(ServerRequest request) {
        logger.info("Received login request");

        return request.bodyToMono(LoginRequest.class)
                .doOnNext(this::validateLoginRequest)
                .flatMap(loginRequest -> {
                    logger.debug("Login request validated for user: {}", loginRequest.getUsername());

                    // Authenticate user
                    if (isValidCredentials(loginRequest.getUsername(), loginRequest.getPassword())) {
                        // Generate token with appropriate roles
                        List<String> roles = getRolesForUser(loginRequest.getUsername());
                        String token = jwtTokenProvider.generateToken(loginRequest.getUsername(), roles);

                        LoginResponse response = new LoginResponse(
                                token,
                                3600000, // 1 hour expiration
                                loginRequest.getUsername()
                        );

                        logger.info("User {} logged in successfully with roles: {}",
                                loginRequest.getUsername(), roles);

                        return ServerResponse.ok().bodyValue(response);
                    } else {
                        logger.warn("Invalid credentials for user: {}", loginRequest.getUsername());
                        return ServerResponse.badRequest()
                                .bodyValue(new com.interview.dto.ErrorResponse("Invalid username or password"));
                    }
                })
                .onErrorResume(error -> {
                    logger.error("Error in login handler", error);
                    return ServerResponse.badRequest()
                            .bodyValue(new com.interview.dto.ErrorResponse(error.getMessage()));
                });
    }

    /**
     * Handles POST /api/v1/auth/test requests
     * Returns a test JWT token for development purposes
     */
    public Mono<ServerResponse> testToken(ServerRequest request) {
        logger.info("Received test token request");

        try {
            String testToken = jwtTokenProvider.generateToken("testuser", List.of("USER"));
            LoginResponse response = new LoginResponse(
                    testToken,
                    3600000,
                    "testuser"
            );

            return ServerResponse.ok().bodyValue(response);
        } catch (Exception ex) {
            logger.error("Error generating test token", ex);
            return ServerResponse.status(500)
                    .bodyValue(new com.interview.dto.ErrorResponse("Failed to generate test token"));
        }
    }

    /**
     * Validates login request
     */
    private void validateLoginRequest(LoginRequest request) {
        if (request.getUsername() == null || request.getUsername().isBlank()) {
            throw new IllegalArgumentException("Username is required");
        }
        if (request.getPassword() == null || request.getPassword().isBlank()) {
            throw new IllegalArgumentException("Password is required");
        }
    }

    /**
     * Validates credentials
     * In production, check against database or identity provider
     */
    private boolean isValidCredentials(String username, String password) {
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
     * Gets roles for user
     * In production, retrieve from database
     */
    private List<String> getRolesForUser(String username) {
        if ("admin".equals(username)) {
            return List.of("ADMIN", "USER");
        }
        return List.of("USER");
    }
}

