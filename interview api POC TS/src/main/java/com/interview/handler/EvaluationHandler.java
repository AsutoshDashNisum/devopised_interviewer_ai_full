package com.interview.handler;

import com.interview.dto.CandidateEvaluationRequest;
import com.interview.dto.ErrorResponse;
import com.interview.dto.FullEvaluationRequest;
import com.interview.service.EvaluationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.codec.DecodingException;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebInputException;
import reactor.core.publisher.Mono;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * EvaluationHandler - WebFlux functional handler
 * Handles HTTP requests for evaluation endpoints
 * Validates input and delegates to EvaluationService
 * Includes a simple in-memory rate limiter to prevent API spamming
 */
@Component
public class EvaluationHandler {

    private static final Logger logger = LoggerFactory.getLogger(EvaluationHandler.class);
    private final EvaluationService evaluationService;

    // Simple global rate limiter for POC: 1 request per 5 seconds
    private final AtomicLong lastRequestTime = new AtomicLong(0);
    private static final long MIN_REQUEST_INTERVAL_MS = 5000;

    public EvaluationHandler(EvaluationService evaluationService) {
        this.evaluationService = evaluationService;
    }

    /**
     * Handles POST /api/v1/evaluate requests
     * Evaluates candidate only (no interviewer evaluation)
     */
    public Mono<ServerResponse> evaluateCandidate(ServerRequest request) {
        logger.info("Received candidate evaluation request");

        if (!checkRateLimit()) {
            return ServerResponse.status(429)
                    .bodyValue(
                            new ErrorResponse("Rate limit exceeded. Please wait a few seconds between evaluations."));
        }

        return request.bodyToMono(CandidateEvaluationRequest.class)
                .doOnNext(this::validateCandidateRequest)
                .flatMap(evaluateRequest -> {
                    logger.debug("Request validated: seniority={}", evaluateRequest.getSeniority());
                    return evaluationService.evaluateCandidate(evaluateRequest)
                            .flatMap(response -> ServerResponse.ok().bodyValue(response));
                })
                .onErrorResume(error -> handleError(error));
    }

    /**
     * Handles POST /api/v1/evaluate/full requests
     * Evaluates candidate with optional interviewer evaluation
     */
    public Mono<ServerResponse> evaluateFull(ServerRequest request) {
        logger.info("Received full evaluation request");

        if (!checkRateLimit()) {
            return ServerResponse.status(429)
                    .bodyValue(
                            new ErrorResponse("Rate limit exceeded. Please wait a few seconds between evaluations."));
        }

        return request.bodyToMono(FullEvaluationRequest.class)
                .doOnNext(this::validateFullRequest)
                .flatMap(evaluateRequest -> {
                    logger.debug("Request validated: seniority={}, evaluateInterviewer={}",
                            evaluateRequest.getSeniority(), evaluateRequest.getEvaluateInterviewer());
                    return evaluationService.evaluateFull(evaluateRequest)
                            .flatMap(response -> ServerResponse.ok().bodyValue(response));
                })
                .onErrorResume(error -> handleError(error));
    }

    /**
     * Simple thread-safe rate limiter check
     * 
     * @return true if request is allowed, false if rate limited
     */
    private boolean checkRateLimit() {
        long now = System.currentTimeMillis();
        long last = lastRequestTime.get();

        if (now - last < MIN_REQUEST_INTERVAL_MS) {
            return false;
        }

        return lastRequestTime.compareAndSet(last, now);
    }

    /**
     * Validates candidate evaluation request
     */
    private void validateCandidateRequest(CandidateEvaluationRequest request) {
        if (request.getJobDescription() == null || request.getJobDescription().isBlank()) {
            throw new IllegalArgumentException("jobDescription is required");
        }
        if (request.getInterviewTranscript() == null || request.getInterviewTranscript().isBlank()) {
            throw new IllegalArgumentException("interviewTranscript is required");
        }
        if (request.getSeniority() == null || request.getSeniority().isBlank()) {
            throw new IllegalArgumentException("seniority is required");
        }
        if (!isValidSeniority(request.getSeniority())) {
            throw new IllegalArgumentException("seniority must be 'junior', 'mid', or 'senior'");
        }
    }

    /**
     * Validates full evaluation request
     */
    private void validateFullRequest(FullEvaluationRequest request) {
        if (request.getJobDescription() == null || request.getJobDescription().isBlank()) {
            throw new IllegalArgumentException("jobDescription is required");
        }
        if (request.getInterviewTranscript() == null || request.getInterviewTranscript().isBlank()) {
            throw new IllegalArgumentException("interviewTranscript is required");
        }
        if (request.getSeniority() == null || request.getSeniority().isBlank()) {
            throw new IllegalArgumentException("seniority is required");
        }
        if (!isValidSeniority(request.getSeniority())) {
            throw new IllegalArgumentException("seniority must be 'junior', 'mid', or 'senior'");
        }
    }

    /**
     * Validates seniority level
     */
    private boolean isValidSeniority(String seniority) {
        return seniority.equalsIgnoreCase("junior") ||
                seniority.equalsIgnoreCase("mid") ||
                seniority.equalsIgnoreCase("senior");
    }

    /**
     * Centralized error handling for evaluation endpoints
     * Maps exceptions to appropriate HTTP status codes
     */
    private Mono<ServerResponse> handleError(Throwable error) {
        logger.error("Error in evaluation endpoint", error);

        // JSON deserialization errors (invalid JSON)
        if (error instanceof DecodingException || error instanceof ServerWebInputException) {
            return ServerResponse.badRequest()
                    .bodyValue(new ErrorResponse("Invalid JSON format or missing required fields"));
        }

        // IllegalArgumentException - validation failures
        if (error instanceof IllegalArgumentException) {
            return ServerResponse.badRequest()
                    .bodyValue(new ErrorResponse(error.getMessage()));
        }

        // UnsupportedOperationException - AI not yet implemented
        if (error instanceof UnsupportedOperationException) {
            return ServerResponse.status(503)
                    .bodyValue(new ErrorResponse(error.getMessage()));
        }

        // Default server error for other exceptions
        return ServerResponse.status(500)
                .bodyValue(new ErrorResponse("Internal server error: " + error.getMessage()));
    }

}
