package com.interview.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Skill evaluation details
 * Part of candidate evaluation response
 */
public class SkillEvaluation {

    @JsonProperty("name")
    private String name;

    @JsonProperty("score")
    private Integer score; // 0-100

    @JsonProperty("evidence")
    private String evidence;

    // Constructors
    public SkillEvaluation() {
    }

    public SkillEvaluation(String name, Integer score, String evidence) {
        this.name = name;
        this.score = score;
        this.evidence = evidence;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getEvidence() {
        return evidence;
    }

    public void setEvidence(String evidence) {
        this.evidence = evidence;
    }

}
