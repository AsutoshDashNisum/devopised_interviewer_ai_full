package com.interview.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import java.util.List;

/**
 * Candidate evaluation response DTO
 * Returned by POST /api/v1/evaluate endpoint
 */
public class CandidateEvaluationResponse {

    @JsonProperty("status")
    private String status;

    @JsonProperty("type")
    private String type;

    @JsonProperty("overallScore")
    private Integer overallScore;

    @JsonProperty("verdict")
    private String verdict; // "strong_hire", "hire", "borderline", "reject"

    @JsonProperty("skillsEvaluation")
    private List<SkillEvaluation> skillsEvaluation;

    @JsonProperty("technicalScore")
    private Integer technicalScore;

    @JsonProperty("communicationScore")
    private Integer communicationScore;

    @JsonProperty("problemSolvingScore")
    private Integer problemSolvingScore;

    @JsonProperty("strengths")
    private List<String> strengths;

    @JsonProperty("weaknesses")
    private List<String> weaknesses;

    @JsonProperty("riskAreas")
    private List<String> riskAreas;

    @JsonProperty("seniorityAlignment")
    private String seniorityAlignment;

    @JsonProperty("summary")
    private String summary;

    @JsonProperty("evaluatedAt")
    private String evaluatedAt;

    // Constructors
    public CandidateEvaluationResponse() {
        this.status = "success";
        this.type = "candidate";
        this.evaluatedAt = Instant.now().toString();
    }

    // Getters and Setters
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getOverallScore() {
        return overallScore;
    }

    public void setOverallScore(Integer overallScore) {
        this.overallScore = overallScore;
    }

    public String getVerdict() {
        return verdict;
    }

    public void setVerdict(String verdict) {
        this.verdict = verdict;
    }

    public List<SkillEvaluation> getSkillsEvaluation() {
        return skillsEvaluation;
    }

    public void setSkillsEvaluation(List<SkillEvaluation> skillsEvaluation) {
        this.skillsEvaluation = skillsEvaluation;
    }

    public Integer getTechnicalScore() {
        return technicalScore;
    }

    public void setTechnicalScore(Integer technicalScore) {
        this.technicalScore = technicalScore;
    }

    public Integer getCommunicationScore() {
        return communicationScore;
    }

    public void setCommunicationScore(Integer communicationScore) {
        this.communicationScore = communicationScore;
    }

    public Integer getProblemSolvingScore() {
        return problemSolvingScore;
    }

    public void setProblemSolvingScore(Integer problemSolvingScore) {
        this.problemSolvingScore = problemSolvingScore;
    }

    public List<String> getStrengths() {
        return strengths;
    }

    public void setStrengths(List<String> strengths) {
        this.strengths = strengths;
    }

    public List<String> getWeaknesses() {
        return weaknesses;
    }

    public void setWeaknesses(List<String> weaknesses) {
        this.weaknesses = weaknesses;
    }

    public List<String> getRiskAreas() {
        return riskAreas;
    }

    public void setRiskAreas(List<String> riskAreas) {
        this.riskAreas = riskAreas;
    }

    public String getSeniorityAlignment() {
        return seniorityAlignment;
    }

    public void setSeniorityAlignment(String seniorityAlignment) {
        this.seniorityAlignment = seniorityAlignment;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getEvaluatedAt() {
        return evaluatedAt;
    }

    public void setEvaluatedAt(String evaluatedAt) {
        this.evaluatedAt = evaluatedAt;
    }

}
