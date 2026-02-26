package com.interview.service.llm;

import com.interview.config.AppProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.List;
import java.util.Map;

/**
 * Gemini LLM Service implementation
 * Real implementation using Google Gemini API with exponential backoff for rate
 * limits
 */
@Service
@ConditionalOnProperty(name = "ai.provider", havingValue = "gemini")
public class GeminiLLMService implements LLMService {

    private static final Logger logger = LoggerFactory.getLogger(GeminiLLMService.class);
    private final WebClient webClient;
    private final AppProperties aiProperties;

    public GeminiLLMService(AppProperties aiProperties, WebClient.Builder webClientBuilder) {
        this.aiProperties = aiProperties;
        this.webClient = webClientBuilder.baseUrl("https://generativelanguage.googleapis.com").build();
        logger.info("Initializing Gemini LLM Service with model: {}", aiProperties.getModel());
    }

    private final java.util.concurrent.atomic.AtomicInteger currentKeyIndex = new java.util.concurrent.atomic.AtomicInteger(
            0);

    @Override
    public Mono<String> evaluate(String prompt) {
        logger.info("Gemini LLM Service: Evaluating prompt");

        List<String> apiKeys = aiProperties.getApiKeys();
        String model = aiProperties.getModel();

        if (apiKeys == null || apiKeys.isEmpty()) {
            return Mono.error(new IllegalStateException("Gemini API keys not configured"));
        }

        return Mono.defer(() -> {
            int index = currentKeyIndex.get() % apiKeys.size();
            String apiKey = apiKeys.get(index);

            // Construct Gemini API request body
            Map<String, Object> requestBody = Map.of(
                    "contents", List.of(
                            Map.of("parts", List.of(Map.of("text", prompt)))),
                    "generationConfig", Map.of(
                            "temperature", aiProperties.getTemperature(),
                            "topP", 1.0,
                            "maxOutputTokens", 4096,
                            "responseMimeType", "application/json"));

            return webClient.post()
                    .uri(uriBuilder -> uriBuilder
                            .path("/v1beta/models/{model}:generateContent")
                            .queryParam("key", apiKey)
                            .build(model))
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .map(this::extractTextFromResponse);
        })
                .retryWhen(Retry.backoff(apiKeys.size() * 2, Duration.ofSeconds(2))
                        .filter(throwable -> {
                            if (throwable instanceof WebClientResponseException responseException) {
                                int status = responseException.getStatusCode().value();
                                return status == 403 || status == 429 || status == 500 || status == 503
                                        || status == 504;
                            }
                            return false;
                        })
                        .doBeforeRetry(retrySignal -> {
                            int nextIndex = currentKeyIndex.incrementAndGet() % apiKeys.size();
                            logger.warn("Gemini API error ({}). Rotating to API key #{} and retrying... Attempt: {}",
                                    ((WebClientResponseException) retrySignal.failure()).getStatusCode(),
                                    nextIndex,
                                    retrySignal.totalRetries() + 1);
                        }))
                .doOnError(e -> logger.error("Error calling Gemini API after rotation retries: {}", e.getMessage()));
    }

    @SuppressWarnings("unchecked")
    private String extractTextFromResponse(Map<String, Object> response) {
        try {
            // Extract text from Gemini response structure:
            // candidates[0].content.parts[0].text
            List<Map<String, Object>> candidates = (List<Map<String, Object>>) response.get("candidates");
            if (candidates == null || candidates.isEmpty()) {
                throw new RuntimeException("No candidates in Gemini response");
            }
            Map<String, Object> firstCandidate = candidates.get(0);
            Map<String, Object> content = (Map<String, Object>) firstCandidate.get("content");
            List<Map<String, Object>> parts = (List<Map<String, Object>>) content.get("parts");
            return (String) parts.get(0).get("text");
        } catch (Exception e) {
            logger.error("Failed to parse Gemini response: {}", response, e);
            throw new RuntimeException("Failed to parse Gemini response: " + e.getMessage());
        }
    }
}
