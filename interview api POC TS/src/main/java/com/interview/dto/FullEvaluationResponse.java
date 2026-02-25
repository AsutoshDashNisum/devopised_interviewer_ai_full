package com.interview.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;

/**
 * Full evaluation response DTO
 * Returned by POST /api/v1/evaluate/full endpoint
 * Includes both candidate and optional interviewer evaluation
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FullEvaluationResponse {

    @JsonProperty("status")
    private String status;

    @JsonProperty("candidateEvaluation")
    private CandidateEvaluationResponse candidateEvaluation;

    @JsonProperty("interviewerEvaluation")
    private InterviewerEvaluationResponse interviewerEvaluation;

    @JsonProperty("evaluatedAt")
    private String evaluatedAt;

    // Constructors
    public FullEvaluationResponse() {
        this.status = "success";
        this.evaluatedAt = Instant.now().toString();
    }

    public FullEvaluationResponse(CandidateEvaluationResponse candidateEvaluation,
                                InterviewerEvaluationResponse interviewerEvaluation) {
        this.status = "success";
        this.candidateEvaluation = candidateEvaluation;
        this.interviewerEvaluation = interviewerEvaluation;
        this.evaluatedAt = Instant.now().toString();
    }

    // Getters and Setters
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public CandidateEvaluationResponse getCandidateEvaluation() {
        return candidateEvaluation;
    }

    public void setCandidateEvaluation(CandidateEvaluationResponse candidateEvaluation) {
        this.candidateEvaluation = candidateEvaluation;
    }

    public InterviewerEvaluationResponse getInterviewerEvaluation() {
        return interviewerEvaluation;
    }

    public void setInterviewerEvaluation(InterviewerEvaluationResponse interviewerEvaluation) {
        this.interviewerEvaluation = interviewerEvaluation;
    }

    public String getEvaluatedAt() {
        return evaluatedAt;
    }

    public void setEvaluatedAt(String evaluatedAt) {
        this.evaluatedAt = evaluatedAt;
    }

}

