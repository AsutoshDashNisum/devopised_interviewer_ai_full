package com.interview.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Complete evaluation response DTO
 * Contains candidate evaluation, optional interviewer evaluation, and metadata
 * Matches the API contract exactly
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EvaluateResponse {

    @JsonProperty("candidate")
    private CandidateEvaluation candidate;

    @JsonProperty("interviewer")
    private InterviewerEvaluation interviewer;

    @JsonProperty("meta")
    private Meta meta;

    // Constructors
    public EvaluateResponse() {
    }

    public EvaluateResponse(CandidateEvaluation candidate, InterviewerEvaluation interviewer, Meta meta) {
        this.candidate = candidate;
        this.interviewer = interviewer;
        this.meta = meta;
    }

    // Getters and Setters
    public CandidateEvaluation getCandidate() {
        return candidate;
    }

    public void setCandidate(CandidateEvaluation candidate) {
        this.candidate = candidate;
    }

    public InterviewerEvaluation getInterviewer() {
        return interviewer;
    }

    public void setInterviewer(InterviewerEvaluation interviewer) {
        this.interviewer = interviewer;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

}

