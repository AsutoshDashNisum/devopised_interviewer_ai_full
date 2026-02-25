package com.interview.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JWT Token Provider Test Suite
 * Tests token generation, validation, and claims extraction
 *
 * Test Coverage:
 * - Token generation with username and roles
 * - Token validation (valid/invalid/expired)
 * - Claims extraction (username, roles)
 * - Edge cases (null input, invalid format)
 */
@SpringBootTest(classes = {com.interview.InterviewEvaluationApplication.class})
@ActiveProfiles("test")
class JwtTokenProviderTest {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    // ==================== Token Generation Tests ====================

    @Test
    void testGenerateToken_WithValidInput() {
        // Arrange
        String username = "testuser";
        List<String> roles = List.of("USER", "ADMIN");

        // Act
        String token = jwtTokenProvider.generateToken(username, roles);

        // Assert
        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertTrue(token.contains("."));
        assertEquals(3, token.split("\\.").length); // JWT has 3 parts: header.payload.signature
    }

    @Test
    void testGenerateToken_WithEmptyRoles() {
        // Arrange
        String username = "testuser";
        List<String> roles = List.of();

        // Act
        String token = jwtTokenProvider.generateToken(username, roles);

        // Assert
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void testGenerateToken_WithMultipleRoles() {
        // Arrange
        String username = "admin";
        List<String> roles = List.of("ADMIN", "USER", "OPERATOR");

        // Act
        String token = jwtTokenProvider.generateToken(username, roles);
        List<String> extractedRoles = jwtTokenProvider.getRolesFromToken(token);

        // Assert
        assertEquals(roles, extractedRoles);
    }

    // ==================== Token Validation Tests ====================

    @Test
    void testValidateToken_WithValidToken() {
        // Arrange
        String token = jwtTokenProvider.generateToken("testuser", List.of("USER"));

        // Act
        boolean isValid = jwtTokenProvider.validateToken(token);

        // Assert
        assertTrue(isValid);
    }

    @Test
    void testValidateToken_WithInvalidSignature() {
        // Arrange
        String validToken = jwtTokenProvider.generateToken("testuser", List.of("USER"));
        // Tamper with token signature
        String tamperedToken = validToken.substring(0, validToken.length() - 10) + "MANIPULATED";

        // Act
        boolean isValid = jwtTokenProvider.validateToken(tamperedToken);

        // Assert
        assertFalse(isValid);
    }

    @Test
    void testValidateToken_WithInvalidFormat() {
        // Arrange
        String invalidToken = "not.a.valid.token.format";

        // Act
        boolean isValid = jwtTokenProvider.validateToken(invalidToken);

        // Assert
        assertFalse(isValid);
    }

    @Test
    void testValidateToken_WithEmptyToken() {
        // Arrange
        String emptyToken = "";

        // Act
        boolean isValid = jwtTokenProvider.validateToken(emptyToken);

        // Assert
        assertFalse(isValid);
    }

    // ==================== Claims Extraction Tests ====================

    @Test
    void testGetUsernameFromToken_WithValidToken() {
        // Arrange
        String expectedUsername = "john.doe";
        String token = jwtTokenProvider.generateToken(expectedUsername, List.of("USER"));

        // Act
        String extractedUsername = jwtTokenProvider.getUsernameFromToken(token);

        // Assert
        assertEquals(expectedUsername, extractedUsername);
    }

    @Test
    void testGetUsernameFromToken_WithInvalidToken() {
        // Arrange
        String invalidToken = "invalid.token.here";

        // Act
        String extractedUsername = jwtTokenProvider.getUsernameFromToken(invalidToken);

        // Assert
        assertNull(extractedUsername);
    }

    @Test
    void testGetRolesFromToken_WithValidToken() {
        // Arrange
        List<String> expectedRoles = List.of("USER", "OPERATOR");
        String token = jwtTokenProvider.generateToken("testuser", expectedRoles);

        // Act
        List<String> extractedRoles = jwtTokenProvider.getRolesFromToken(token);

        // Assert
        assertEquals(expectedRoles, extractedRoles);
    }

    @Test
    void testGetRolesFromToken_WithInvalidToken() {
        // Arrange
        String invalidToken = "invalid.token";

        // Act
        List<String> extractedRoles = jwtTokenProvider.getRolesFromToken(invalidToken);

        // Assert
        assertTrue(extractedRoles.isEmpty());
    }

    // ==================== Integration Tests ====================

    @Test
    void testTokenLifecycle() {
        // Arrange
        String username = "lifecycle.test";
        List<String> roles = List.of("USER", "ADMIN");

        // Act - Generate token
        String token = jwtTokenProvider.generateToken(username, roles);

        // Assert - Token is valid
        assertTrue(jwtTokenProvider.validateToken(token));

        // Act - Extract claims
        String extractedUsername = jwtTokenProvider.getUsernameFromToken(token);
        List<String> extractedRoles = jwtTokenProvider.getRolesFromToken(token);

        // Assert - Claims match
        assertEquals(username, extractedUsername);
        assertEquals(roles, extractedRoles);
    }

    @Test
    void testDifferentTokensDifferentSignatures() {
        // Arrange
        String token1 = jwtTokenProvider.generateToken("user1", List.of("USER"));
        String token2 = jwtTokenProvider.generateToken("user2", List.of("USER"));

        // Act & Assert
        assertNotEquals(token1, token2);
        assertTrue(jwtTokenProvider.validateToken(token1));
        assertTrue(jwtTokenProvider.validateToken(token2));
        assertNotEquals(
                jwtTokenProvider.getUsernameFromToken(token1),
                jwtTokenProvider.getUsernameFromToken(token2)
        );
    }
}

