package com.interview.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Application configuration properties
 * Loaded from application.yml
 * Provides type-safe access to configuration values
 */
@Component
@ConfigurationProperties(prefix = "ai")
public class AppProperties {

    private boolean enabled;
    private String provider;
    private java.util.List<String> apiKeys;
    private String model;
    private double temperature;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public java.util.List<String> getApiKeys() {
        return apiKeys;
    }

    public void setApiKeys(java.util.List<String> apiKeys) {
        this.apiKeys = apiKeys;
    }

    public String getApiKey() {
        if (apiKeys == null || apiKeys.isEmpty()) {
            return null;
        }
        return apiKeys.get(0);
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

}
