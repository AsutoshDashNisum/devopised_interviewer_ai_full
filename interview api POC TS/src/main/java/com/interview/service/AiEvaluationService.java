package com.interview.service;

import com.interview.config.AppProperties;
import com.interview.dto.CandidateEvaluationRequest;
import com.interview.dto.CandidateEvaluationResponse;
import com.interview.dto.FullEvaluationRequest;
import com.interview.dto.FullEvaluationResponse;
import com.interview.dto.InterviewerEvaluationResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.regex.Pattern;

/**
 * AiEvaluationService - Real evaluation using AI/LLM provider
 *
 * Active when: ai.enabled=true
 */
@Service
@ConditionalOnProperty(name = "ai.enabled", havingValue = "true")
public class AiEvaluationService implements EvaluationService {

        private static final Logger logger = LoggerFactory.getLogger(AiEvaluationService.class);
        private final AppProperties appProperties;
        private final com.interview.service.llm.LLMService llmService;
        private final com.fasterxml.jackson.databind.ObjectMapper objectMapper;

        // Pattern for common filler words and sounds
        private static final Pattern FILLER_PATTERN = Pattern
                        .compile("(?i)\\b(uh|um|hmm|okay|actually|basically|like|you know|right|so)\\b");

        public AiEvaluationService(AppProperties appProperties, com.interview.service.llm.LLMService llmService,
                        com.fasterxml.jackson.databind.ObjectMapper objectMapper) {
                this.appProperties = appProperties;
                this.llmService = llmService;
                this.objectMapper = objectMapper;
                logger.info("AiEvaluationService initialized. Provider: {}, Model: {}",
                                appProperties.getProvider(), appProperties.getModel());
        }

        @Override
        public Mono<CandidateEvaluationResponse> evaluateCandidate(CandidateEvaluationRequest request) {
                logger.info("AiEvaluationService: Evaluating candidate using {} model", appProperties.getModel());

                String sanitizedTranscript = sanitizeTranscript(request.getInterviewTranscript());

                String prompt = com.interview.constant.PromptTemplate.GEMINI_EVALUATION_PROMPT
                                .replace("{JOB_DESCRIPTION}", request.getJobDescription())
                                .replace("{INTERVIEW_TRANSCRIPT}", sanitizedTranscript)
                                .replace("{SENIORITY_LEVEL}", request.getSeniority());

                return llmService.evaluate(prompt)
                                .flatMap(jsonResponse -> {
                                        try {
                                                String cleanJson = cleanJsonResponse(jsonResponse);
                                                return Mono.just(objectMapper.readValue(cleanJson,
                                                                CandidateEvaluationResponse.class));
                                        } catch (Exception e) {
                                                logger.error("Failed to parse Candidate LLM response. Raw response: {}",
                                                                jsonResponse, e);
                                                return Mono.error(new RuntimeException(
                                                                "Failed to parse candidate evaluation report: "
                                                                                + e.getMessage()));
                                        }
                                });
        }

        @Override
        public Mono<FullEvaluationResponse> evaluateFull(FullEvaluationRequest request) {
                logger.info("AiEvaluationService: Performing full evaluation (evaluateInterviewer={})",
                                request.getEvaluateInterviewer());

                Mono<CandidateEvaluationResponse> candidateMono = evaluateCandidate(new CandidateEvaluationRequest(
                                request.getJobDescription(),
                                request.getInterviewTranscript(),
                                request.getSeniority()));

                if (Boolean.TRUE.equals(request.getEvaluateInterviewer())) {
                        Mono<InterviewerEvaluationResponse> interviewerMono = evaluateInterviewer(request);
                        return Mono.zip(candidateMono, interviewerMono)
                                        .map(tuple -> new FullEvaluationResponse(tuple.getT1(), tuple.getT2()));
                } else {
                        return candidateMono.map(candidateEval -> new FullEvaluationResponse(candidateEval, null));
                }
        }

        private Mono<InterviewerEvaluationResponse> evaluateInterviewer(FullEvaluationRequest request) {
                logger.info("AiEvaluationService: Evaluating interviewer using {} model", appProperties.getModel());

                String sanitizedTranscript = sanitizeTranscript(request.getInterviewTranscript());

                String prompt = com.interview.constant.PromptTemplate.GEMINI_INTERVIEWER_EVALUATION_PROMPT
                                .replace("{JOB_DESCRIPTION}", request.getJobDescription())
                                .replace("{INTERVIEW_TRANSCRIPT}", sanitizedTranscript);

                return llmService.evaluate(prompt)
                                .flatMap(jsonResponse -> {
                                        try {
                                                String cleanJson = cleanJsonResponse(jsonResponse);
                                                return Mono.just(objectMapper.readValue(cleanJson,
                                                                InterviewerEvaluationResponse.class));
                                        } catch (Exception e) {
                                                logger.error("Failed to parse Interviewer LLM response. Raw response: {}",
                                                                jsonResponse, e);
                                                return Mono.error(new RuntimeException(
                                                                "Failed to parse interviewer evaluation report: "
                                                                                + e.getMessage()));
                                        }
                                });
        }

        private String cleanJsonResponse(String jsonResponse) {
                if (jsonResponse == null)
                        return "{}";
                String cleanJson = jsonResponse.trim();
                if (cleanJson.startsWith("```json")) {
                        cleanJson = cleanJson.substring(7);
                } else if (cleanJson.startsWith("```")) {
                        cleanJson = cleanJson.substring(3);
                }
                if (cleanJson.endsWith("```")) {
                        cleanJson = cleanJson.substring(0, cleanJson.length() - 3);
                }
                return cleanJson.trim();
        }

        /**
         * Sanitizes transcript to reduce token usage and noise
         */
        private String sanitizeTranscript(String transcript) {
                if (transcript == null || transcript.isBlank())
                        return "";

                // 1. Remove common filler words
                String sanitized = FILLER_PATTERN.matcher(transcript).replaceAll("");

                // 2. Normalize whitespace
                sanitized = sanitized.replaceAll("\\s+", " ").trim();

                // 3. Limit size (optional POC restraint, e.g., max 30000 chars)
                if (sanitized.length() > 30000) {
                        logger.warn("Transcript too long ({} chars). Truncating to 30000.", sanitized.length());
                        sanitized = sanitized.substring(0, 30000) + "... [TRUNCATED]";
                }

                return sanitized;
        }
}
