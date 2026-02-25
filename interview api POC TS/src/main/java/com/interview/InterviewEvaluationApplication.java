package com.interview;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main Spring Boot Application Entry Point
 * Bootstraps the Interview Evaluation API server
 */
@SpringBootApplication
public class InterviewEvaluationApplication {

    public static void main(String[] args) {
        SpringApplication.run(InterviewEvaluationApplication.class, args);
    }

}

