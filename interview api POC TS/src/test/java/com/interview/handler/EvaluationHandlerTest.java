package com.interview.handler;

import com.interview.dto.CandidateEvaluationRequest;
import com.interview.dto.CandidateEvaluationResponse;
import com.interview.dto.ErrorResponse;
import com.interview.dto.FullEvaluationRequest;
import com.interview.dto.FullEvaluationResponse;
import com.interview.dto.InterviewerEvaluationResponse;
import com.interview.dto.SkillEvaluation;
import com.interview.service.EvaluationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * EvaluationHandler Integration Tests
 * Tests HTTP endpoint behavior, request/response validation, and error handling
 * Tests both /api/v1/evaluate and /api/v1/evaluate/full endpoints
 * Uses WebTestClient for reactive testing with feature flag architecture
 */
@SpringBootTest(classes = {com.interview.InterviewEvaluationApplication.class})
@AutoConfigureWebTestClient
@ActiveProfiles("test")
class EvaluationHandlerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private EvaluationService evaluationService;

    // ==================== /api/v1/evaluate Tests ====================

    @Test
    void testEvaluateCandidateWithValidRequest() {
        // Arrange
        CandidateEvaluationRequest request = new CandidateEvaluationRequest(
                "Senior Full Stack Engineer with 5+ years TypeScript, React, Node.js",
                "Interviewer: Tell me about your TypeScript experience. Candidate: I have 6 years of professional TypeScript experience.",
                "senior"
        );

        CandidateEvaluationResponse mockResponse = new CandidateEvaluationResponse();
        mockResponse.setStatus("success");
        mockResponse.setType("candidate");
        mockResponse.setOverallScore(75);
        mockResponse.setVerdict("hire");
        mockResponse.setCommunicationScore(8);
        mockResponse.setProblemSolvingScore(7);
        mockResponse.setSkillsEvaluation(Arrays.asList(
                new SkillEvaluation("Java", 8, "Strong OOP principles"),
                new SkillEvaluation("Spring Boot", 8, "Production experience")
        ));
        mockResponse.setStrengths(Arrays.asList("Problem solving", "Communication"));
        mockResponse.setWeaknesses(Arrays.asList("DevOps", "Architecture"));
        mockResponse.setSummary("Well-rounded mid-level engineer");

        when(evaluationService.evaluateCandidate(any()))
                .thenReturn(Mono.just(mockResponse));

        // Act & Assert
        webTestClient.post()
                .uri("/api/v1/evaluate")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.status").isEqualTo("success")
                .jsonPath("$.overallScore").isEqualTo(75)
                .jsonPath("$.verdict").isEqualTo("hire")
                .jsonPath("$.communicationScore").isEqualTo(8);
    }

    @Test
    void testEvaluateCandidateWithMissingSeniority() {
        // Arrange - Missing seniority field
        String requestBody = """
            {
              "jobDescription": "Senior Full Stack Engineer",
              "interviewTranscript": "This is a longer transcript with sufficient content"
            }
            """;

        // Act & Assert - Should return 400 for missing required field
        webTestClient.post()
                .uri("/api/v1/evaluate")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.status").isEqualTo("error");
    }

    @Test
    void testEvaluateCandidateWithInvalidSeniority() {
        // Arrange - Invalid seniority level
        String requestBody = """
            {
              "jobDescription": "Senior Full Stack Engineer",
              "interviewTranscript": "This is a longer transcript with sufficient content",
              "seniority": "invalid"
            }
            """;

        // Act & Assert - Should return 400 for invalid seniority
        webTestClient.post()
                .uri("/api/v1/evaluate")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.status").isEqualTo("error")
                .jsonPath("$.message").isNotEmpty();
    }

    @Test
    void testEvaluateCandidateWithInvalidJson() {
        // Arrange - Malformed JSON
        String invalidJson = "{ invalid json }";

        // Act & Assert - Should return 400 for invalid JSON
        webTestClient.post()
                .uri("/api/v1/evaluate")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(invalidJson)
                .exchange()
                .expectStatus().isBadRequest();
    }

    // ==================== /api/v1/evaluate/full Tests ====================

    @Test
    void testEvaluateFullWithoutInterviewerAssessment() {
        // Arrange
        FullEvaluationRequest request = new FullEvaluationRequest(
                "Senior Backend Engineer",
                "Interviewer: Tell me about microservices. Candidate: I have 8 years of experience.",
                "senior",
                false
        );

        CandidateEvaluationResponse candidateEval = new CandidateEvaluationResponse();
        candidateEval.setStatus("success");
        candidateEval.setOverallScore(85);
        candidateEval.setVerdict("strong_hire");

        FullEvaluationResponse mockResponse = new FullEvaluationResponse(candidateEval, null);

        when(evaluationService.evaluateFull(any()))
                .thenReturn(Mono.just(mockResponse));

        // Act & Assert
        webTestClient.post()
                .uri("/api/v1/evaluate/full")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.status").isEqualTo("success")
                .jsonPath("$.candidateEvaluation.overallScore").isEqualTo(85)
                .jsonPath("$.interviewerEvaluation").doesNotExist();
    }

    @Test
    void testEvaluateFullWithInterviewerAssessment() {
        // Arrange
        FullEvaluationRequest request = new FullEvaluationRequest(
                "Senior Backend Engineer",
                "Interviewer: Tell me about microservices. Candidate: I have 8 years of experience.",
                "senior",
                true
        );

        CandidateEvaluationResponse candidateEval = new CandidateEvaluationResponse();
        candidateEval.setStatus("success");
        candidateEval.setOverallScore(85);
        candidateEval.setVerdict("strong_hire");

        InterviewerEvaluationResponse interviewerEval = new InterviewerEvaluationResponse(
                8, 8, 8, "low",
                Arrays.asList("Good questions", "Clear communication"),
                Arrays.asList("Could probe deeper"),
                "Well-structured interview"
        );

        FullEvaluationResponse mockResponse = new FullEvaluationResponse(candidateEval, interviewerEval);

        when(evaluationService.evaluateFull(any()))
                .thenReturn(Mono.just(mockResponse));

        // Act & Assert
        webTestClient.post()
                .uri("/api/v1/evaluate/full")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.status").isEqualTo("success")
                .jsonPath("$.candidateEvaluation.overallScore").isEqualTo(85)
                .jsonPath("$.interviewerEvaluation.overallScore").isEqualTo(8)
                .jsonPath("$.interviewerEvaluation.biasRisk").isEqualTo("low");
    }

    // ==================== Health & Root Endpoint Tests ====================

    @Test
    void testHealthEndpoint() {
        // Act & Assert
        webTestClient.get()
                .uri("/health")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.status").isEqualTo("ok");
    }

    @Test
    void testRootEndpoint() {
        // Act & Assert
        webTestClient.get()
                .uri("/")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.service").isEqualTo("Interview Evaluation API");
    }

    @Test
    void testNotFoundEndpoint() {
        // Act & Assert
        webTestClient.get()
                .uri("/unknown-endpoint")
                .exchange()
                .expectStatus().isNotFound();
    }

}
