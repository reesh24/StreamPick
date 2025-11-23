package com.streampick.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

/**
 * Configuration for RestTemplate to call external services (Python ML service)
 */
@Configuration
public class RestTemplateConfig {
    
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
                .setConnectTimeout(Duration.ofSeconds(5))  // 5 second connection timeout
                .setReadTimeout(Duration.ofSeconds(10))     // 10 second read timeout
                .build();
    }
}