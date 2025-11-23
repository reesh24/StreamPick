package com.streampick.controller;

import com.streampick.dto.RecommendationRequest;
import com.streampick.dto.RecommendationResponse;
import com.streampick.service.RecommendationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for movie recommendations
 * 
 * This is the core endpoint that powers StreamPick!
 */
@RestController
@RequestMapping("/api/recommendations")
@CrossOrigin(origins = "${cors.allowed.origins}")
@RequiredArgsConstructor
@Slf4j
public class RecommendationController {

    private final RecommendationService recommendationService;

    /**
     * Get personalized movie recommendations
     * 
     * Takes user's mood and available time, applies ML-based content filtering,
     * and returns top 5 movie recommendations with match scores and reasoning.
     * 
     * @param request User preferences (mood and time available)
     * @return List of recommended movies with match scores and reasoning
     */
    @PostMapping
    public ResponseEntity<RecommendationResponse> getRecommendation(
            @Valid @RequestBody RecommendationRequest request) {
        
        log.info("POST /api/recommendations - Request: mood={}, timeAvailable={}", 
                request.getMood(), request.getTimeAvailable());
        
        try {
            RecommendationResponse response = recommendationService.getRecommendation(request);
            
            log.info("Recommendations generated: count={}, source={}", 
                    response.getRecommendations().size(),
                    response.getSource());
            
            return ResponseEntity.ok(response);
            
        } catch (RuntimeException e) {
            log.error("Error generating recommendation", e);
            throw e; // Will be handled by GlobalExceptionHandler
            
        } catch (Exception e) {
            log.error("Unexpected error generating recommendation", e);
            throw new RuntimeException("Failed to generate recommendation. Please try again.");
        }
    }
}

