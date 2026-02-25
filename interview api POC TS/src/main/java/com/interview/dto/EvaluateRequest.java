package com.interview.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Request DTO for /api/v1/evaluate endpoint
 * Contains job description, interview transcript, and evaluation preferences
 * Validated using Jakarta Bean Validation (formerly javax.validation)
 */
public class EvaluateRequest {

    @NotBlank(message = "Job description is required")
    @Size(min = 10, message = "Job description must be at least 10 characters")
    @JsonProperty("jobDescription")
    private String jobDescription;

    @NotBlank(message = "Interview transcript is required")
    @Size(min = 20, message = "Interview transcript must be at least 20 characters")
    @JsonProperty("interviewTranscript")
    private String interviewTranscript;

    @NotBlank(message = "Seniority level is required")
    @JsonProperty("seniorityLevel")
    private String seniorityLevel;

    @NotNull(message = "includeInterviewerEvaluation must be specified")
    @JsonProperty("includeInterviewerEvaluation")
    private Boolean includeInterviewerEvaluation = false;

    // Constructors
    public EvaluateRequest() {
    }

    public EvaluateRequest(String jobDescription, String interviewTranscript,
                          String seniorityLevel, Boolean includeInterviewerEvaluation) {
        this.jobDescription = jobDescription;
        this.interviewTranscript = interviewTranscript;
        this.seniorityLevel = seniorityLevel;
        this.includeInterviewerEvaluation = includeInterviewerEvaluation;
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

    public String getSeniorityLevel() {
        return seniorityLevel;
    }

    public void setSeniorityLevel(String seniorityLevel) {
        this.seniorityLevel = seniorityLevel;
    }

    public Boolean getIncludeInterviewerEvaluation() {
        return includeInterviewerEvaluation;
    }

    public void setIncludeInterviewerEvaluation(Boolean includeInterviewerEvaluation) {
        this.includeInterviewerEvaluation = includeInterviewerEvaluation;
    }

}

