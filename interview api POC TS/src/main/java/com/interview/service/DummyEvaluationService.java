package com.interview.service;

import com.interview.dto.CandidateEvaluationRequest;
import com.interview.dto.CandidateEvaluationResponse;
import com.interview.dto.FullEvaluationRequest;
import com.interview.dto.FullEvaluationResponse;
import com.interview.dto.InterviewerEvaluationResponse;
import com.interview.dto.SkillEvaluation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

/**
 * DummyEvaluationService - Deterministic evaluation without AI
 *
 * Active when: ai.enabled=false
 *
 * Returns consistent, deterministic dummy responses based on seniority level.
 * No random values. No external API calls.
 *
 * This allows testing the API wiring while AI integration is disabled.
 */
@Service
@ConditionalOnProperty(name = "ai.enabled", havingValue = "false", matchIfMissing = true)
public class DummyEvaluationService implements EvaluationService {

        private static final Logger logger = LoggerFactory.getLogger(DummyEvaluationService.class);

        @Override
        public Mono<CandidateEvaluationResponse> evaluateCandidate(CandidateEvaluationRequest request) {
                logger.info("DummyEvaluationService: Evaluating candidate (seniority={})", request.getSeniority());

                return Mono.fromCallable(() -> generateCandidateEvaluation(request.getSeniority()));
        }

        @Override
        public Mono<FullEvaluationResponse> evaluateFull(FullEvaluationRequest request) {
                logger.info("DummyEvaluationService: Full evaluation (seniority={}, evaluateInterviewer={})",
                                request.getSeniority(), request.getEvaluateInterviewer());

                return Mono.fromCallable(() -> {
                        CandidateEvaluationResponse candidateEval = generateCandidateEvaluation(request.getSeniority());

                        InterviewerEvaluationResponse interviewerEval = null;
                        if (request.getEvaluateInterviewer() != null && request.getEvaluateInterviewer()) {
                                interviewerEval = generateInterviewerEvaluation();
                        }

                        return new FullEvaluationResponse(candidateEval, interviewerEval);
                });
        }

        /**
         * Generates deterministic candidate evaluation based on seniority level
         */
        private CandidateEvaluationResponse generateCandidateEvaluation(String seniority) {
                CandidateEvaluationResponse response = new CandidateEvaluationResponse();

                if ("junior".equalsIgnoreCase(seniority)) {
                        response.setOverallScore(62);
                        response.setTechnicalScore(55);
                        response.setVerdict("borderline");
                        response.setCommunicationScore(75);
                        response.setProblemSolvingScore(50);
                        response.setSkillsEvaluation(Arrays.asList(
                                        new SkillEvaluation("JavaScript", 70,
                                                        "Solid fundamentals, good DOM manipulation"),
                                        new SkillEvaluation("React", 60, "Basic component knowledge"),
                                        new SkillEvaluation("REST APIs", 50, "Limited production experience")));
                        response.setStrengths(Arrays.asList(
                                        "Quick learner",
                                        "Good communication skills",
                                        "Demonstrates curiosity about technologies"));
                        response.setWeaknesses(Arrays.asList(
                                        "Limited production experience",
                                        "Needs mentoring on best practices",
                                        "Database design knowledge is basic"));
                        response.setRiskAreas(Arrays.asList(
                                        "No production deployment experience",
                                        "Weak SQL knowledge"));
                        response.setSeniorityAlignment(
                                        "Shows promise but currently fits better at an entry-level/internship role than Junior (requires heavy mentorship).");
                        response.setSummary(
                                        "Promising junior engineer with solid fundamentals. Would benefit from mentorship on production systems and architectural patterns.");
                } else if ("mid".equalsIgnoreCase(seniority)) {
                        response.setOverallScore(75);
                        response.setTechnicalScore(78);
                        response.setVerdict("hire");
                        response.setCommunicationScore(80);
                        response.setProblemSolvingScore(70);
                        response.setSkillsEvaluation(Arrays.asList(
                                        new SkillEvaluation("Java", 80, "Strong OOP principles and design patterns"),
                                        new SkillEvaluation("Spring Boot", 80,
                                                        "Production experience with microservices"),
                                        new SkillEvaluation("SQL", 70, "Good database design and optimization"),
                                        new SkillEvaluation("System Design", 60,
                                                        "Can architect medium-scale systems")));
                        response.setStrengths(Arrays.asList(
                                        "Strong technical depth",
                                        "Good problem-solving approach",
                                        "Effective team communication",
                                        "Production experience"));
                        response.setWeaknesses(Arrays.asList(
                                        "Limited large-scale system experience",
                                        "Could improve on distributed systems concepts"));
                        response.setRiskAreas(Arrays.asList(
                                        "Limited experience with high-traffic systems"));
                        response.setSeniorityAlignment(
                                        "Strongly aligns with Mid-level expectations. Ready for Senior-level responsibilities within 12-18 months.");
                        response.setSummary(
                                        "Well-rounded mid-level engineer ready for senior responsibilities. Strong technical skills and proven ability to deliver production systems.");
                } else { // senior
                        response.setOverallScore(85);
                        response.setTechnicalScore(90);
                        response.setVerdict("strong_hire");
                        response.setCommunicationScore(90);
                        response.setProblemSolvingScore(90);
                        response.setSkillsEvaluation(Arrays.asList(
                                        new SkillEvaluation("System Architecture", 90,
                                                        "Excellent design of scalable systems"),
                                        new SkillEvaluation("Leadership", 80, "Mentors junior engineers effectively"),
                                        new SkillEvaluation("Cloud Infrastructure", 90, "Extensive AWS/GCP experience"),
                                        new SkillEvaluation("Technical Decision-Making", 80,
                                                        "Makes well-informed architectural choices")));
                        response.setStrengths(Arrays.asList(
                                        "Exceptional technical depth",
                                        "Strong leadership and mentoring skills",
                                        "Excellent communication with stakeholders",
                                        "Proven track record with large-scale systems",
                                        "Clear technical vision"));
                        response.setWeaknesses(Arrays.asList(
                                        "None apparent from interview"));
                        response.setRiskAreas(Arrays.asList(
                                        "None identified"));
                        response.setSeniorityAlignment(
                                        "Exceptional Senior candidate. Demonstrates leadership qualities found in Staff/Principal engineers.");
                        response.setSummary(
                                        "Exceptional senior engineer with deep technical expertise and leadership qualities. Ready for principal engineer or architect roles. Strong hire.");
                }

                return response;
        }

        /**
         * Generates deterministic interviewer evaluation
         */
        private InterviewerEvaluationResponse generateInterviewerEvaluation() {
                return new InterviewerEvaluationResponse(
                                8, // overallScore
                                8, // questionQuality
                                8, // communicationClarity
                                "low", // biasRisk
                                Arrays.asList(
                                                "Asked relevant technical questions",
                                                "Good mix of behavioral and technical probes",
                                                "Clear communication of expectations"),
                                Arrays.asList(
                                                "Could probe deeper on system design",
                                                "Follow-up questions could be more specific"),
                                "Interviewer conducted a well-structured interview with appropriate depth for the role level.");
        }

}
