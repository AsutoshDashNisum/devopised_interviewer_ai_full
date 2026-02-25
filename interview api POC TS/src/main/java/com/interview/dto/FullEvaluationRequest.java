package com.interview.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

/**
 * Request DTO for POST /api/v1/evaluate/full endpoint
 * Candidate evaluation with optional interviewer assessment
 */
public class FullEvaluationRequest {

    @NotBlank(message = "Job description is required")
    @JsonProperty("jobDescription")
    private String jobDescription;

    @NotBlank(message = "Interview transcript is required")
    @JsonProperty("interviewTranscript")
    private String interviewTranscript;

    @NotBlank(message = "Seniority level is required")
    @JsonProperty("seniority")
    private String seniority; // "junior", "mid", "senior"

    @JsonProperty("evaluateInterviewer")
    private Boolean evaluateInterviewer = false;

    // Constructors
    public FullEvaluationRequest() {
    }

    public FullEvaluationRequest(String jobDescription, String interviewTranscript,
                               String seniority, Boolean evaluateInterviewer) {
        this.jobDescription = jobDescription;
        this.interviewTranscript = interviewTranscript;
        this.seniority = seniority;
        this.evaluateInterviewer = evaluateInterviewer;
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

    public Boolean getEvaluateInterviewer() {
        return evaluateInterviewer;
    }

    public void setEvaluateInterviewer(Boolean evaluateInterviewer) {
        this.evaluateInterviewer = evaluateInterviewer;
    }

}

