package com.streampick;

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
        SpringApplication.run(StreamPickApplication.class, args);
    }
}

