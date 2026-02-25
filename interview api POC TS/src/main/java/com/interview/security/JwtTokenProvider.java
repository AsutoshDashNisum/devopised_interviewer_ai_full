package com.interview.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

/**
 * JWT Token Provider
 * Handles generation, validation, and extraction of claims from JWT tokens
 *
 * Features:
 * - Token generation with user roles and custom claims
 * - Token validation with signature verification
 * - Token expiration checks
 * - Claims extraction (username, roles, etc.)
 */
@Component
public class JwtTokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    @Value("${jwt.secret:your-secret-key-change-in-production}")
    private String jwtSecret;

    @Value("${jwt.expiration:3600000}")
    private long jwtExpirationMs;

    /**
     * Generates JWT token for the given username and roles
     */
    public String generateToken(String username, List<String> roles) {
        SecretKey key = getSigningKey();
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationMs);

        return Jwts.builder()
                .subject(username)
                .claim("roles", roles)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(key)
                .compact();
    }

    /**
     * Validates JWT token signature and expiration
     *
     * @param token the JWT token to validate
     * @return true if token is valid, false otherwise
     */
    public boolean validateToken(String token) {
        try {
            SecretKey key = getSigningKey();
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (JwtException ex) {
            logger.debug("Invalid JWT token: {}", ex.getMessage());
        } catch (IllegalArgumentException ex) {
            logger.debug("JWT claims string is empty: {}", ex.getMessage());
        }
        return false;
    }

    /**
     * Extracts username (subject) from JWT token
     */
    public String getUsernameFromToken(String token) {
        try {
            SecretKey key = getSigningKey();
            Claims claims = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return claims.getSubject();
        } catch (JwtException ex) {
            logger.debug("Failed to extract username from token: {}", ex.getMessage());
            return null;
        }
    }

    /**
     * Extracts roles from JWT token claims
     */
    @SuppressWarnings("unchecked")
    public List<String> getRolesFromToken(String token) {
        try {
            SecretKey key = getSigningKey();
            Claims claims = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return (List<String>) claims.get("roles");
        } catch (JwtException ex) {
            logger.debug("Failed to extract roles from token: {}", ex.getMessage());
            return List.of();
        }
    }

    /**
     * Gets the signing key from the configured secret
     * Uses HMAC-512 algorithm
     */
    private SecretKey getSigningKey() {
        byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}

