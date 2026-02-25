package com.interview.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Login Request DTO
 * Used for user authentication
 */
public class LoginRequest {

    @JsonProperty(required = true)
    private String username;

    @JsonProperty(required = true)
    private String password;

    // Constructors
    public LoginRequest() {}

    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Getters & Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

