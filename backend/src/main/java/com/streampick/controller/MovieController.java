package com.streampick.controller;

import com.streampick.model.Movie;
import com.streampick.service.ContentstackService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for movie operations
 */
@RestController
@RequestMapping("/api/movies")
@CrossOrigin(origins = "${cors.allowed.origins}")
@RequiredArgsConstructor
@Slf4j
public class MovieController {

    private final ContentstackService contentstackService;

    /**
     * Get all available movies
     * 
     * @return List of all movies
     */
    @GetMapping
    public ResponseEntity<List<Movie>> getAllMovies() {
        log.info("GET /api/movies - Fetching all movies");
        
        try {
            List<Movie> movies = contentstackService.getAllMovies();
            log.info("Successfully retrieved {} movies", movies.size());
            return ResponseEntity.ok(movies);
        } catch (Exception e) {
            log.error("Error fetching movies", e);
            throw new RuntimeException("Failed to fetch movies: " + e.getMessage());
        }
    }

    /**
     * Get movies filtered by mood
     * 
     * @param mood The mood to filter by (cozy, thrilling, laugh, deep, escape, chill)
     * @return List of movies matching the mood
     */
    @GetMapping("/mood/{mood}")
    public ResponseEntity<List<Movie>> getMoviesByMood(@PathVariable String mood) {
        log.info("GET /api/movies/mood/{} - Fetching movies by mood", mood);
        
        try {
            List<Movie> movies = contentstackService.getMoviesByMood(mood);
            log.info("Found {} movies for mood: {}", movies.size(), mood);
            
            if (movies.isEmpty()) {
                log.warn("No movies found for mood: {}", mood);
            }
            
            return ResponseEntity.ok(movies);
        } catch (Exception e) {
            log.error("Error fetching movies for mood: {}", mood, e);
            throw new RuntimeException("Failed to fetch movies for mood: " + mood);
        }
    }
}

