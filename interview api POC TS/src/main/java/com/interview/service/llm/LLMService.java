package com.interview.service.llm;

import reactor.core.publisher.Mono;

/**
 * LLM Service abstraction interface
 * Decouples evaluation logic from specific LLM provider
 * Allows easy mocking and switching between providers (OpenAI, Anthropic, etc.)
 */
public interface LLMService {

    /**
     * Evaluates a prompt using the configured LLM
     * Returns deterministic JSON response
     *
     * @param prompt the evaluation prompt
     * @return Mono containing the LLM response
     */
    Mono<String> evaluate(String prompt);

}

