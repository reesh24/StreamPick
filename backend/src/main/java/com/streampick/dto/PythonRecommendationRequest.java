package com.streampick.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * DTO to send request to Python ML service
 */
@Data
@Builder
public class PythonRecommendationRequest {
    
    private String mood;
    
    @JsonProperty("time_available")
    private Integer timeAvailable;
    
    private List<MovieInput> movies;  // All movies from Contentstack
    
    @JsonProperty("top_n")
    private Integer topN;
    
    @JsonProperty("user_id")
    private String userId;
    
    /**
     * Movie data to send to Python
     */
    @Data
    @Builder
    public static class MovieInput {
        private String title;
        private Integer year;
        private Integer runtime;
        private Double rating;
        private List<String> genre;
        @JsonProperty("mood_tags")
        private List<String> moodTags;
        private List<String> platforms;
        private String description;
        @JsonProperty("ai_description")
        private String aiDescription;
        @JsonProperty("image_url")
        private String imageUrl;
    }
}