package com.interview.service;

import com.interview.dto.CandidateEvaluationRequest;
import com.interview.dto.CandidateEvaluationResponse;
import com.interview.dto.FullEvaluationRequest;
import com.interview.dto.FullEvaluationResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * EvaluationService Unit Tests
 * Tests the DummyEvaluationService implementation
 * Verifies deterministic behavior and correct response structure
 * Uses Reactor's StepVerifier for reactive testing
 */
@SpringBootTest(classes = {com.interview.InterviewEvaluationApplication.class})
@ActiveProfiles("test")
class EvaluationServiceTest {

    @Autowired
    private EvaluationService evaluationService;

    // ==================== Candidate Evaluation Tests ====================

    @Test
    void testEvaluateCandidateJunior() {
        // Arrange
        CandidateEvaluationRequest request = new CandidateEvaluationRequest(
                "Junior Frontend Developer - JavaScript, React",
                "Interviewer: Tell me about React. Candidate: I learned React in a bootcamp.",
                "junior"
        );

        // Act & Assert
        StepVerifier.create(evaluationService.evaluateCandidate(request))
                .assertNext(response -> {
                    assertThat(response).isNotNull();
                    assertThat(response.getStatus()).isEqualTo("success");
                    assertThat(response.getType()).isEqualTo("candidate");
                    assertThat(response.getOverallScore()).isEqualTo(62);
                    assertThat(response.getVerdict()).isEqualTo("borderline");
                    assertThat(response.getCommunicationScore()).isEqualTo(7);
                    assertThat(response.getProblemSolvingScore()).isEqualTo(5);
                    assertThat(response.getSkillsEvaluation()).isNotEmpty();
                    assertThat(response.getStrengths()).isNotEmpty();
                    assertThat(response.getWeaknesses()).isNotEmpty();
                })
                .verifyComplete();
    }

    @Test
    void testEvaluateCandidateMid() {
        // Arrange
        CandidateEvaluationRequest request = new CandidateEvaluationRequest(
                "Mid-Level Engineer - Python, Django, PostgreSQL",
                "Interviewer: Describe your Django experience. Candidate: I've used Django professionally for 3 years.",
                "mid"
        );

        // Act & Assert
        StepVerifier.create(evaluationService.evaluateCandidate(request))
                .assertNext(response -> {
                    assertThat(response).isNotNull();
                    assertThat(response.getStatus()).isEqualTo("success");
                    assertThat(response.getOverallScore()).isEqualTo(75);
                    assertThat(response.getVerdict()).isEqualTo("hire");
                    assertThat(response.getCommunicationScore()).isEqualTo(8);
                    assertThat(response.getProblemSolvingScore()).isEqualTo(7);
                })
                .verifyComplete();
    }

    @Test
    void testEvaluateCandidateSenior() {
        // Arrange
        CandidateEvaluationRequest request = new CandidateEvaluationRequest(
                "Senior Backend Engineer - Java, Spring Boot, microservices",
                "Interviewer: Tell me about large-scale systems. Candidate: I have 8 years of experience designing microservices.",
                "senior"
        );

        // Act & Assert
        StepVerifier.create(evaluationService.evaluateCandidate(request))
                .assertNext(response -> {
                    assertThat(response).isNotNull();
                    assertThat(response.getStatus()).isEqualTo("success");
                    assertThat(response.getOverallScore()).isEqualTo(85);
                    assertThat(response.getVerdict()).isEqualTo("strong_hire");
                    assertThat(response.getCommunicationScore()).isEqualTo(9);
                    assertThat(response.getProblemSolvingScore()).isEqualTo(9);
                })
                .verifyComplete();
    }

    @Test
    void testEvaluateCandidateDeterministic() {
        // Arrange
        CandidateEvaluationRequest request = new CandidateEvaluationRequest(
                "Senior Engineer",
                "Interviewer: Your experience? Candidate: 8 years.",
                "senior"
        );

        // Act - Call evaluation twice
        var first = evaluationService.evaluateCandidate(request);
        var second = evaluationService.evaluateCandidate(request);

        // Assert - Both should have identical scores (deterministic)
        StepVerifier.create(first)
                .assertNext(response -> assertThat(response.getOverallScore()).isEqualTo(85))
                .verifyComplete();

        StepVerifier.create(second)
                .assertNext(response -> assertThat(response.getOverallScore()).isEqualTo(85))
                .verifyComplete();
    }

    // ==================== Full Evaluation Tests ====================

    @Test
    void testEvaluateFullWithoutInterviewer() {
        // Arrange
        FullEvaluationRequest request = new FullEvaluationRequest(
                "Senior Backend Engineer",
                "Interviewer: Tell me about microservices. Candidate: I have 8 years.",
                "senior",
                false
        );

        // Act & Assert
        StepVerifier.create(evaluationService.evaluateFull(request))
                .assertNext(response -> {
                    assertThat(response).isNotNull();
                    assertThat(response.getStatus()).isEqualTo("success");
                    assertThat(response.getCandidateEvaluation()).isNotNull();
                    assertThat(response.getCandidateEvaluation().getOverallScore()).isEqualTo(85);
                    assertThat(response.getInterviewerEvaluation()).isNull();
                })
                .verifyComplete();
    }

    @Test
    void testEvaluateFullWithInterviewer() {
        // Arrange
        FullEvaluationRequest request = new FullEvaluationRequest(
                "Senior Backend Engineer",
                "Interviewer: Tell me about microservices. Candidate: I have 8 years.",
                "senior",
                true
        );

        // Act & Assert
        StepVerifier.create(evaluationService.evaluateFull(request))
                .assertNext(response -> {
                    assertThat(response).isNotNull();
                    assertThat(response.getStatus()).isEqualTo("success");
                    assertThat(response.getCandidateEvaluation()).isNotNull();
                    assertThat(response.getCandidateEvaluation().getOverallScore()).isEqualTo(85);
                    assertThat(response.getInterviewerEvaluation()).isNotNull();
                    assertThat(response.getInterviewerEvaluation().getOverallScore()).isEqualTo(8);
                    assertThat(response.getInterviewerEvaluation().getBiasRisk()).isEqualTo("low");
                })
                .verifyComplete();
    }

}
