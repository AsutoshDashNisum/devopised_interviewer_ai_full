package com.interview.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;

/**
 * Error response DTO
 * Returned when API encounters an error
 */
public class ErrorResponse {

    @JsonProperty("status")
    private String status;

    @JsonProperty("message")
    private String message;

    @JsonProperty("timestamp")
    private String timestamp;

    // Constructors
    public ErrorResponse() {
        this.status = "error";
        this.timestamp = Instant.now().toString();
    }

    public ErrorResponse(String message) {
        this.status = "error";
        this.message = message;
        this.timestamp = Instant.now().toString();
    }

    public ErrorResponse(String message, String timestamp) {
        this.status = "error";
        this.message = message;
        this.timestamp = timestamp;
    }

    // Getters and Setters
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

}

