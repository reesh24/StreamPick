package com.streampick.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;
import java.util.Map;

/**
 * DTO to receive response from Python ML service
 */
@Data
public class PythonRecommendationResponse {
    
    private List<PythonRecommendation> recommendations;

    @JsonProperty("total_candidates")
    private Integer totalCandidates;

    @JsonProperty("filters_applied")
    private Map<String, Object> filtersApplied;

    @Data
    public static class PythonRecommendation {
        private PythonMovie movie;
        private String reason;

        @JsonProperty("match_score")
        private Double matchScore;
    }

    @Data
    public static class PythonMovie {
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

        @JsonProperty("similarity_score")
        private Double similarityScore;
        
        @JsonProperty("image_url")
        private String imageUrl;
        
        @JsonProperty("image")
        private Map<String, Object> image;  // For reconstructing image object
    }
}
