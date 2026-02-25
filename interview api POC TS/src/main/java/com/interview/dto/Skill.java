package com.interview.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Skill DTO
 * Represents a technical skill with proficiency score
 */
public class Skill {

    @JsonProperty("name")
    private String name;

    @JsonProperty("score")
    private Integer score;

    // Constructors
    public Skill() {
    }

    public Skill(String name, Integer score) {
        this.name = name;
        this.score = score;
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

}

