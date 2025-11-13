package com.streampick.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

/**
 * Request DTO for movie recommendation
 */
@Data
public class RecommendationRequest {
    
    @NotBlank(message = "Mood is required")
    private String mood;
    
    @NotNull(message = "Time available is required")
    @Positive(message = "Time available must be positive")
    private Integer timeAvailable; // in minutes
    
    private String userId; // Optional for future personalization
}

