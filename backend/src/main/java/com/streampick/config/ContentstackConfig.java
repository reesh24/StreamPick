package com.streampick.config;

import com.contentstack.sdk.Config;
import com.contentstack.sdk.Stack;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for Contentstack SDK integration
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
     * @return Configured Stack instance for content delivery
     */
    @Bean
    public Stack contentstackStack() {
        log.info("Initializing Contentstack SDK...");
        
        Config config = new Config();
        config.setHost("cdn.contentstack.io");
        
        Stack stack = new Stack(apiKey, deliveryToken, environment);
        stack.setConfig(config);
        
        log.info("Contentstack SDK initialized successfully for environment: {}", environment);
        
        return stack;
    }
}

