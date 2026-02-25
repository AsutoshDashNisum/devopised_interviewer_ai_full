package com.interview.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Meta information DTO
 * Contains summary, seniority match, and confidence level
 * Always included in the evaluation response
 */
public class Meta {

    @JsonProperty("overallSummary")
    private String overallSummary;

    @JsonProperty("seniorityMatch")
    private String seniorityMatch;

    @JsonProperty("confidenceLevel")
    private String confidenceLevel;

    // Constructors
    public Meta() {
    }

    public Meta(String overallSummary, String seniorityMatch, String confidenceLevel) {
        this.overallSummary = overallSummary;
        this.seniorityMatch = seniorityMatch;
        this.confidenceLevel = confidenceLevel;
    }

    // Getters and Setters
    public String getOverallSummary() {
        return overallSummary;
    }

    public void setOverallSummary(String overallSummary) {
        this.overallSummary = overallSummary;
    }

    public String getSeniorityMatch() {
        return seniorityMatch;
    }

    public void setSeniorityMatch(String seniorityMatch) {
        this.seniorityMatch = seniorityMatch;
    }

    public String getConfidenceLevel() {
        return confidenceLevel;
    }

    public void setConfidenceLevel(String confidenceLevel) {
        this.confidenceLevel = confidenceLevel;
    }

}

