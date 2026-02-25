package com.interview.service.llm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Mock LLM Service implementation
 * Returns deterministic, hardcoded responses without calling external APIs
 * Used for development, testing, and when OPENAI_API_KEY is not provided
 */
@Service
@org.springframework.boot.autoconfigure.condition.ConditionalOnProperty(name = "ai.enabled", havingValue = "false", matchIfMissing = true)
public class MockLLMService implements LLMService {

  private static final Logger logger = LoggerFactory.getLogger(MockLLMService.class);

  @Override
  public Mono<String> evaluate(String prompt) {
    logger.info("MockLLMService: Evaluating prompt (mock mode - no API calls)");

    // Return deterministic mock response that matches the API contract
    String mockResponse = """
        {
          "candidate": {
            "name": null,
            "overallScore": 75,
            "hiringRecommendation": "Hire",
            "jdFit": "The candidate demonstrates good alignment with the role requirements, showing proficiency in core technologies and relevant experience.",
            "skills": [
              { "name": "TypeScript", "score": 85 },
              { "name": "React", "score": 78 },
              { "name": "Node.js", "score": 72 },
              { "name": "System Design", "score": 68 }
            ],
            "strengths": [
              "Strong problem-solving approach",
              "Clear communication of technical concepts",
              "Demonstrated ability to learn new technologies quickly"
            ],
            "gaps": [
              "Limited experience with distributed systems",
              "Could improve on algorithmic optimization",
              "Needs more exposure to cloud infrastructure"
            ],
            "riskAreas": [
              "Lack of hands-on DevOps experience",
              "Limited contribution to large-scale projects"
            ]
          },
          "meta": {
            "overallSummary": "Solid mid-level engineer with strong fundamentals and communication skills. Good fit for the role with potential for growth.",
            "seniorityMatch": "Aligns well with Senior (5-10 years) expectation, though slightly below the upper range of experience.",
            "confidenceLevel": "High"
          }
        }
        """;

    // Simulate network latency
    return Mono.delay(java.time.Duration.ofMillis(100))
        .then(Mono.just(mockResponse));
  }

}
