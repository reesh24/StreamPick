package com.streampick.dto;

import com.streampick.model.Movie;
import lombok.Builder;
import lombok.Data;
import java.util.List;
/**
 * Response DTO for movie recommendation
 */
@Data
@Builder
public class RecommendationResponse {

    private List<MovieRecommendation> recommendations;
    private int totalCandidates;
    private String source;

    @Data
    @Builder
    public static class MovieRecommendation {
        private Movie movie;
        private String aiReason;
        private Double matchScore;
    }
    

}

