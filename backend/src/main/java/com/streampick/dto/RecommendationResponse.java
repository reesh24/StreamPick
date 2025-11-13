package com.streampick.dto;

import com.streampick.model.Movie;
import lombok.Builder;
import lombok.Data;

/**
 * Response DTO for movie recommendation
 */
@Data
@Builder
public class RecommendationResponse {
    
    private Movie movie;
    private String aiReason;
    private Double matchScore;
}

