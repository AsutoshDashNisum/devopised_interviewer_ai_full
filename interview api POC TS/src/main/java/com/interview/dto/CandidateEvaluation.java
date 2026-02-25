package com.interview.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Candidate evaluation response DTO
 * Contains candidate assessment including skills, strengths, gaps, and hiring recommendation
 */
public class CandidateEvaluation {

    @JsonProperty("name")
    private String name;

    @JsonProperty("overallScore")
    private Integer overallScore;

    @JsonProperty("hiringRecommendation")
    private String hiringRecommendation;

    @JsonProperty("jdFit")
    private String jdFit;

    @JsonProperty("skills")
    private List<Skill> skills;

    @JsonProperty("strengths")
    private List<String> strengths;

    @JsonProperty("gaps")
    private List<String> gaps;

    @JsonProperty("riskAreas")
    private List<String> riskAreas;

    // Constructors
    public CandidateEvaluation() {
    }

    public CandidateEvaluation(String name, Integer overallScore, String hiringRecommendation,
                              String jdFit, List<Skill> skills, List<String> strengths,
                              List<String> gaps, List<String> riskAreas) {
        this.name = name;
        this.overallScore = overallScore;
        this.hiringRecommendation = hiringRecommendation;
        this.jdFit = jdFit;
        this.skills = skills;
        this.strengths = strengths;
        this.gaps = gaps;
        this.riskAreas = riskAreas;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getOverallScore() {
        return overallScore;
    }

    public void setOverallScore(Integer overallScore) {
        this.overallScore = overallScore;
    }

    public String getHiringRecommendation() {
        return hiringRecommendation;
    }

    public void setHiringRecommendation(String hiringRecommendation) {
        this.hiringRecommendation = hiringRecommendation;
    }

    public String getJdFit() {
        return jdFit;
    }

    public void setJdFit(String jdFit) {
        this.jdFit = jdFit;
    }

    public List<Skill> getSkills() {
        return skills;
    }

    public void setSkills(List<Skill> skills) {
        this.skills = skills;
    }

    public List<String> getStrengths() {
        return strengths;
    }

    public void setStrengths(List<String> strengths) {
        this.strengths = strengths;
    }

    public List<String> getGaps() {
        return gaps;
    }

    public void setGaps(List<String> gaps) {
        this.gaps = gaps;
    }

    public List<String> getRiskAreas() {
        return riskAreas;
    }

    public void setRiskAreas(List<String> riskAreas) {
        this.riskAreas = riskAreas;
    }

}

