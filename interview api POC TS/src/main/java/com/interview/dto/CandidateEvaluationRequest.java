package com.interview.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

/**
 * Request DTO for POST /api/v1/evaluate endpoint
 * Simple candidate evaluation without interviewer assessment
 */
public class CandidateEvaluationRequest {

    @NotBlank(message = "Job description is required")
    @JsonProperty("jobDescription")
    private String jobDescription;

    @NotBlank(message = "Interview transcript is required")
    @JsonProperty("interviewTranscript")
    private String interviewTranscript;

    @NotBlank(message = "Seniority level is required")
    @JsonProperty("seniority")
    private String seniority; // "junior", "mid", "senior"

    // Constructors
    public CandidateEvaluationRequest() {
    }

    public CandidateEvaluationRequest(String jobDescription, String interviewTranscript, String seniority) {
        this.jobDescription = jobDescription;
        this.interviewTranscript = interviewTranscript;
        this.seniority = seniority;
    }

    // Getters and Setters
    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public String getInterviewTranscript() {
        return interviewTranscript;
    }

    public void setInterviewTranscript(String interviewTranscript) {
        this.interviewTranscript = interviewTranscript;
    }

    public String getSeniority() {
        return seniority;
    }

    public void setSeniority(String seniority) {
        this.seniority = seniority;
    }

}

