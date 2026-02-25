package com.interview.dto;

/**
 * Login Response DTO
 * Contains JWT token and expiration information
 */
public class LoginResponse {

    private String token;
    private long expiresIn;
    private String tokenType;
    private String username;

    // Constructors
    public LoginResponse() {}

    public LoginResponse(String token, long expiresIn, String username) {
        this.token = token;
        this.expiresIn = expiresIn;
        this.tokenType = "Bearer";
        this.username = username;
    }

    // Getters & Setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(long expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

