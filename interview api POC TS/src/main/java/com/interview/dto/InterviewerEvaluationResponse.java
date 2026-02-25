package com.interview.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Interviewer evaluation details
 * Part of full evaluation response when evaluateInterviewer=true
 */
public class InterviewerEvaluationResponse {

    @JsonProperty("overallScore")
    private Integer overallScore; // 0-10

    @JsonProperty("questionQuality")
    private Integer questionQuality; // 0-10

    @JsonProperty("communicationClarity")
    private Integer communicationClarity; // 0-10

    @JsonProperty("biasRisk")
    private String biasRisk; // "low", "medium", "high"

    @JsonProperty("strengths")
    private List<String> strengths;

    @JsonProperty("improvements")
    private List<String> improvements;

    @JsonProperty("summary")
    private String summary;

    // Constructors
    public InterviewerEvaluationResponse() {
    }

    public InterviewerEvaluationResponse(Integer overallScore, Integer questionQuality,
                                       Integer communicationClarity, String biasRisk,
                                       List<String> strengths, List<String> improvements, String summary) {
        this.overallScore = overallScore;
        this.questionQuality = questionQuality;
        this.communicationClarity = communicationClarity;
        this.biasRisk = biasRisk;
        this.strengths = strengths;
        this.improvements = improvements;
        this.summary = summary;
    }

    // Getters and Setters
    public Integer getOverallScore() {
        return overallScore;
    }

    public void setOverallScore(Integer overallScore) {
        this.overallScore = overallScore;
    }

    public Integer getQuestionQuality() {
        return questionQuality;
    }

    public void setQuestionQuality(Integer questionQuality) {
        this.questionQuality = questionQuality;
    }

    public Integer getCommunicationClarity() {
        return communicationClarity;
    }

    public void setCommunicationClarity(Integer communicationClarity) {
        this.communicationClarity = communicationClarity;
    }

    public String getBiasRisk() {
        return biasRisk;
    }

    public void setBiasRisk(String biasRisk) {
        this.biasRisk = biasRisk;
    }

    public List<String> getStrengths() {
        return strengths;
    }

    public void setStrengths(List<String> strengths) {
        this.strengths = strengths;
    }

    public List<String> getImprovements() {
        return improvements;
    }

    public void setImprovements(List<String> improvements) {
        this.improvements = improvements;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

}

