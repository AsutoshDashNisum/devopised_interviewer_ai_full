package com.interview.service;

import com.interview.dto.CandidateEvaluationRequest;
import com.interview.dto.CandidateEvaluationResponse;
import com.interview.dto.FullEvaluationRequest;
import com.interview.dto.FullEvaluationResponse;
import reactor.core.publisher.Mono;

/**
 * EvaluationService interface
 * Contract for candidate and interviewer evaluation implementations
 *
 * Implementations:
 * - AiEvaluationService: Uses AI/LLM provider (when ai.enabled=true)
 * - DummyEvaluationService: Returns deterministic dummy responses (when ai.enabled=false)
 */
public interface EvaluationService {

    /**
     * Evaluates a candidate based on job description and interview transcript
     * Returns deterministic response based on service implementation
     *
     * @param request the evaluation request
     * @return Mono containing the candidate evaluation response
     */
    Mono<CandidateEvaluationResponse> evaluateCandidate(CandidateEvaluationRequest request);

    /**
     * Evaluates candidate with optional interviewer evaluation
     * Controlled by ai.enabled flag
     *
     * @param request the full evaluation request
     * @return Mono containing full evaluation response
     */
    Mono<FullEvaluationResponse> evaluateFull(FullEvaluationRequest request);

}

