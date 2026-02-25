package com.interview.service.llm;

import com.interview.config.AppProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * OpenAI LLM Service implementation
 * Real implementation using OpenAI API with temperature=0 for deterministic
 * output
 * Only created if ai.enabled is true and API key is provided
 */
@Service
@ConditionalOnProperty(name = "ai.provider", havingValue = "openai")
public class OpenAILLMService implements LLMService {

    private static final Logger logger = LoggerFactory.getLogger(OpenAILLMService.class);

    private final AppProperties aiProperties;

    public OpenAILLMService(AppProperties aiProperties) {
        this.aiProperties = aiProperties;
        logger.info("Initializing OpenAI LLM Service with model: {}, provider: {}",
                aiProperties.getModel(), aiProperties.getProvider());
    }

    @Override
    public Mono<String> evaluate(String prompt) {
        logger.debug("OpenAI LLM Service: Evaluating prompt (making API call to OpenAI)");

        // NOTE: This is a placeholder for actual OpenAI integration
        // In production, you would use the OpenAI Java client here

        String apiKey = aiProperties.getApiKey();
        String model = aiProperties.getModel();
        double temperature = aiProperties.getTemperature();

        if (apiKey == null || apiKey.isEmpty() || "DUMMY_API_KEY".equals(apiKey)) {
            logger.warn("OpenAI API key not configured or is dummy, cannot proceed with real API");
            return Mono.error(new IllegalStateException("OpenAI API key not configured"));
        }

        // TODO: Integrate with actual OpenAI client
        // This would involve:
        // 1. Creating OpenAI client with apiKey
        // 2. Calling chat.completions.create() with the prompt
        // 3. Extracting response content
        // 4. Returning it in a Mono wrapper

        logger.warn("OpenAI integration not yet implemented. Set ai.enabled=false to use dummy service.");
        return Mono.error(new UnsupportedOperationException("OpenAI integration not yet implemented"));
    }

}
