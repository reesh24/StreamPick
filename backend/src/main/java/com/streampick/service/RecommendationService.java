package com.streampick.service;

import com.streampick.dto.PythonRecommendationRequest;
import com.streampick.dto.PythonRecommendationResponse;
import com.streampick.dto.RecommendationRequest;
import com.streampick.dto.RecommendationResponse;
import com.streampick.model.Movie;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for generating movie recommendations
 * 
 * Strategy:
 * 1. Try to call Python ML service (content-based filtering with TF-IDF + cosine similarity)
 * 2. If Python fails, fallback to simple Java-based scoring
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class RecommendationService {

    private final ContentstackService contentstackService;
    private final RestTemplate restTemplate;

    @Value("${python.recommendation.service.url}")
    private String pythonServiceUrl;
    
    /**
     * Generate movie recommendations based on user preferences
     * 
     * @param request User's mood and available time
     * @return List of recommended movies with reasoning and match scores
     */
    public RecommendationResponse getRecommendation(RecommendationRequest request) {
        log.info("Generating recommendations for mood: {}, time: {} mins", 
                request.getMood(), request.getTimeAvailable());
        try{
            return getRecommendationsFromPython(request);
        } catch (Exception e) {
            log.warn("Python service unavailable, using fallback: {}", e.getMessage());
            // Fallback to simple Java-based recommendations
            return getRecommendationsFromJavaFallback(request);
        }
    }

    private RecommendationResponse getRecommendationsFromPython(RecommendationRequest request) {

        String url = pythonServiceUrl + "/recommend";

        // Step 1: Fetch ALL movies from Contentstack (with images!)
        List<Movie> allMovies = contentstackService.getAllMovies();
        log.info("Fetched {} movies from Contentstack", allMovies.size());
        
        // Step 2: Convert to Python format
        List<PythonRecommendationRequest.MovieInput> movieInputs = allMovies.stream()
                .map(this::convertToMovieInput)
                .collect(Collectors.toList());
        
        // Step 3: Send to Python ML service
        PythonRecommendationRequest pythonRequest = PythonRecommendationRequest.builder()
            .mood(request.getMood())
            .timeAvailable(request.getTimeAvailable())
            .movies(movieInputs)  // Send all movies with images
            .topN(5)
            .userId(request.getUserId())
            .build();

        log.info("Sending {} movies to Python ML service", movieInputs.size());
        PythonRecommendationResponse response = restTemplate.postForObject(url, pythonRequest, PythonRecommendationResponse.class);
        
        if (response == null || response.getRecommendations() == null) {
            throw new RuntimeException("Python service returned null response");
        }
        
        log.info("Python service returned {} recommendations", response.getRecommendations().size());
        
        // Convert Python response to our response format
        return convertPythonResponse(response);
    }
    
    /**
     * Convert Movie to Python's MovieInput format
     */
    private PythonRecommendationRequest.MovieInput convertToMovieInput(Movie movie) {
        return PythonRecommendationRequest.MovieInput.builder()
                .title(movie.getTitle())
                .year(movie.getYear())
                .runtime(movie.getRuntime())
                .rating(movie.getRating())
                .genre(movie.getGenre())
                .moodTags(movie.getMoodTags())
                .platforms(movie.getPlatforms())
                .description(movie.getDescription())
                .aiDescription(movie.getAiDescription())
                .imageUrl(movie.getImageUrl())  // Include image URL
                .build();
    }

    private RecommendationResponse convertPythonResponse(PythonRecommendationResponse pythonResponse) {
        List<RecommendationResponse.MovieRecommendation> recommendations = 
                pythonResponse.getRecommendations().stream()
                .map(this::convertPythonRecommendation)
                .collect(Collectors.toList());
        
        return RecommendationResponse.builder()
                .recommendations(recommendations)
                .totalCandidates(pythonResponse.getTotalCandidates())
                .source("ml")  // ML-based recommendations
                .build();
    }

    private RecommendationResponse.MovieRecommendation convertPythonRecommendation(
            PythonRecommendationResponse.PythonRecommendation pythonRec) {
        
        PythonRecommendationResponse.PythonMovie pythonMovie = pythonRec.getMovie();
        
        // Create Movie object (Python already has all data including image URL)
        Movie movie = new Movie();
        movie.setTitle(pythonMovie.getTitle());
        movie.setYear(pythonMovie.getYear());
        movie.setRuntime(pythonMovie.getRuntime());
        movie.setRating(pythonMovie.getRating());
        movie.setGenre(pythonMovie.getGenre());
        movie.setMoodTags(pythonMovie.getMoodTags());
        movie.setPlatforms(pythonMovie.getPlatforms());
        movie.setDescription(pythonMovie.getDescription());
        movie.setAiDescription(pythonMovie.getAiDescription());
        
        // Reconstruct image Map from imageUrl (if available)
        if (pythonMovie.getImageUrl() != null) {
            java.util.Map<String, Object> imageMap = new java.util.HashMap<>();
            imageMap.put("url", pythonMovie.getImageUrl());
            movie.setImage(imageMap);
        }
        
        return RecommendationResponse.MovieRecommendation.builder()
                .movie(movie)
                .aiReason(pythonRec.getReason())
                .matchScore(pythonRec.getMatchScore())
                .build();
    }

    private RecommendationResponse getRecommendationsFromJavaFallback(RecommendationRequest request) {
        log.info("Using Java fallback recommendation logic");
        
        // Fetch candidate movies matching mood
        List<Movie> candidates = contentstackService.getMoviesByMood(request.getMood());
        
        if (candidates.isEmpty()) {
            log.warn("No movies found for mood: {}", request.getMood());
            throw new RuntimeException("No movies found matching your preferences. Try a different mood!");
        }

        log.info("Found {} candidate movies", candidates.size());

        // Score and sort movies
        List<Movie> scoredMovies = candidates.stream()
                .sorted(Comparator.comparing((Movie m) -> calculateSimpleScore(m, request)).reversed())
                .limit(5)  // Get top 5
                .collect(Collectors.toList());
        
        // Convert to response format
        List<RecommendationResponse.MovieRecommendation> recommendations = scoredMovies.stream()
                .map(movie -> RecommendationResponse.MovieRecommendation.builder()
                        .movie(movie)
                        .aiReason(movie.getAiDescription())
                        .matchScore(calculateSimpleScore(movie, request))
                        .build())
                .collect(Collectors.toList());
        
        return RecommendationResponse.builder()
                .recommendations(recommendations)
                .totalCandidates(candidates.size())
                .source("fallback")  // Simple fallback
                .build();
    }

    /**
     * Simple scoring algorithm for fallback
     */
    private double calculateSimpleScore(Movie movie, RecommendationRequest request) {
        double score = 0.0;
        
        // Mood match (50 points)
        if (movie.getMoodTags() != null && 
            movie.getMoodTags().stream().anyMatch(tag -> tag.equalsIgnoreCase(request.getMood()))) {
            score += 50.0;
        }
        
        // Rating quality (30 points)
        if (movie.getRating() != null) {
            score += (movie.getRating() / 10.0) * 30.0;
        }
        
        // Runtime fit (20 points)
        if (movie.getRuntime() != null && request.getTimeAvailable() != null) {
            int timeDiff = Math.abs(movie.getRuntime() - request.getTimeAvailable());
            if (timeDiff <= 20) {
                score += 20.0;
            } else if (timeDiff <= 40) {
                score += 10.0;
            }
        }
        
        return Math.min(score, 100.0);
    }
}

