package com.interview.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Interviewer evaluation response DTO
 * Contains assessment of interviewer's performance and question quality
 * Only included in response if includeInterviewerEvaluation = true
 */
public class InterviewerEvaluation {

    @JsonProperty("coreJdCoverage")
    private String coreJdCoverage;

    @JsonProperty("questionQuality")
    private String questionQuality;

    @JsonProperty("followUpNeeded")
    private Boolean followUpNeeded;

    @JsonProperty("followUpQuestions")
    private List<String> followUpQuestions;

    // Constructors
    public InterviewerEvaluation() {
    }

    public InterviewerEvaluation(String coreJdCoverage, String questionQuality,
                                Boolean followUpNeeded, List<String> followUpQuestions) {
        this.coreJdCoverage = coreJdCoverage;
        this.questionQuality = questionQuality;
        this.followUpNeeded = followUpNeeded;
        this.followUpQuestions = followUpQuestions;
    }

    // Getters and Setters
    public String getCoreJdCoverage() {
        return coreJdCoverage;
    }

    public void setCoreJdCoverage(String coreJdCoverage) {
        this.coreJdCoverage = coreJdCoverage;
    }

    public String getQuestionQuality() {
        return questionQuality;
    }

    public void setQuestionQuality(String questionQuality) {
        this.questionQuality = questionQuality;
    }

    public Boolean getFollowUpNeeded() {
        return followUpNeeded;
    }

    public void setFollowUpNeeded(Boolean followUpNeeded) {
        this.followUpNeeded = followUpNeeded;
    }

    public List<String> getFollowUpQuestions() {
        return followUpQuestions;
    }

    public void setFollowUpQuestions(List<String> followUpQuestions) {
        this.followUpQuestions = followUpQuestions;
    }

}

