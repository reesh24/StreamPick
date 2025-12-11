package com.streampick;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * StreamPick Application - Movie Recommendation System
 * 
 * A full-stack application that helps users find the perfect movie
 * in 3 clicks based on mood + available time + contextual intelligence.
 * 
 * @author StreamPick Team
 */
@SpringBootApplication
public class StreamPickApplication {

    public static void main(String[] args) {
        // Load .env file before Spring Boot starts
        Dotenv dotenv = Dotenv.configure()
                .directory(".")
                .ignoreIfMissing()
                .load();
        
        // Set environment variables from .env file
        dotenv.entries().forEach(entry -> 
            System.setProperty(entry.getKey(), entry.getValue())
        );
        
        SpringApplication.run(StreamPickApplication.class, args);
    }
}

