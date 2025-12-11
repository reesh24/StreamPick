package com.streampick.config;

import com.contentstack.sdk.Stack;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for Contentstack SDK integration
 * 
 * Note: Currently disabled as we're using mock data.
 * Enable this when ready to connect to real Contentstack.
 */
@Configuration
@Slf4j
public class ContentstackConfig {

    @Value("${contentstack.api.key}")
    private String apiKey;

    @Value("${contentstack.delivery.token}")
    private String deliveryToken;

    @Value("${contentstack.environment}")
    private String environment;

    /**
     * Creates and configures Contentstack Stack bean
     * 
     * NOTE: Returning null for now since we're using mock data.
     * When ready to use real Contentstack, update this method with proper SDK initialization.
     * 
     * @return Configured Stack instance for content delivery (currently null)
     */
    @Bean
    public Stack contentstackStack() {
        log.info("Contentstack SDK configuration loaded (using mock data)");
        log.info("API Key configured: {}", apiKey != null && !apiKey.isEmpty() ? "Yes" : "No");
        log.info("Environment: {}", environment);
        
        // TODO: Initialize real Contentstack SDK when ready to connect to CMS
        // For now, returning null since ContentstackService uses mock data
        return null;
    }
}

