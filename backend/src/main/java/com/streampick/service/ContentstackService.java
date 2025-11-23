package com.streampick.service;

import com.contentstack.sdk.*;
import com.google.gson.Gson;
import com.streampick.model.Movie;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Service for fetching movies from Contentstack CMS
 * Now integrated with real Contentstack API!
 */
@Service
public class ContentstackService {
    
    private static final Logger log = LoggerFactory.getLogger(ContentstackService.class);

    @Value("${contentstack.api.key}")
    private String apiKey;

    @Value("${contentstack.delivery.token}")
    private String deliveryToken;

    @Value("${contentstack.environment}")
    private String environment;

    /**
     * Get all movies from Contentstack
     * @return List of all movies (empty list if none found or error occurs)
     */
    public List<Movie> getAllMovies() {
        log.info("Fetching all movies from Contentstack");
        
        try {
            // Initialize Contentstack Stack
            Stack stack = Contentstack.stack(apiKey, deliveryToken, environment);
            
            // Query the "movie" content type
            Query query = stack.contentType("movie").query();
            query.includeReference("image");
            
            // Use CountDownLatch to wait for async callback
            final List<Movie> movies = new ArrayList<>();
            final CountDownLatch latch = new CountDownLatch(1);
            final boolean[] success = {false};
            
            // Execute async query with callback
            query.find(new QueryResultsCallBack() {
                @Override
                public void onCompletion(ResponseType responseType, QueryResult queryResult, com.contentstack.sdk.Error error) {
                    try {
                        if (error == null && queryResult != null) {
                            log.info("Contentstack query successful, processing entries...");
                            log.info("Response Type: {}", responseType);
                            log.info("Query Result: {}", queryResult);
                            
                            List<Entry> entries = queryResult.getResultObjects();
                            log.info("Number of entries returned: {}", entries != null ? entries.size() : 0);
                            
                            if (entries != null && !entries.isEmpty()) {
                                for (Entry entry : entries) {
                                    try {
                                        Movie movie = mapEntryToMovie(entry);
                                        if (movie != null) {
                                            movies.add(movie);
                                        }
                                    } catch (Exception e) {
                                        log.error("Error mapping entry to movie: {}", entry.getUid(), e);
                                    }
                                }
                            }
                            
                            success[0] = true;
                            log.info("Successfully fetched {} movies from Contentstack", movies.size());
                        } else {
                            if (error != null) {
                                log.error("Contentstack query failed - Error Code: {}, Error Message: {}", 
                                        error.getErrorCode(), error.getErrorMessage());
                                log.error("Error Details: {}", error.getErrorDetail());
                            } else {
                                log.error("Contentstack query failed: queryResult is null");
                            }
                        }
                    } finally {
                        latch.countDown();
                    }
                }
            });
            
            // Wait for callback to complete (max 10 seconds)
            if (!latch.await(10, TimeUnit.SECONDS)) {
                log.error("Contentstack query timeout after 10 seconds");
                return new ArrayList<>();
            }
            
            if (success[0]) {
                log.info("Returning {} movies", movies.size());
                return movies;
            } else {
                log.warn("No movies fetched from Contentstack, returning empty list");
                return new ArrayList<>();
            }
            
        } catch (InterruptedException e) {
            log.error("Thread interrupted while fetching movies from Contentstack", e);
            Thread.currentThread().interrupt();
            return new ArrayList<>();
        } catch (Exception e) {
            log.error("Error fetching movies from Contentstack", e);
            return new ArrayList<>();
        }
    }

    /**
     * Get movies by mood
     * @param mood The mood to filter by
     * @return List of movies matching the mood (empty list if none found)
     */
    public List<Movie> getMoviesByMood(String mood) {
        log.info("Fetching movies for mood: {}", mood);
        
        if (mood == null || mood.trim().isEmpty()) {
            log.warn("Mood parameter is null or empty");
            return new ArrayList<>();
        }
        
        List<Movie> allMovies = getAllMovies();
        
        if (allMovies.isEmpty()) {
            log.warn("No movies available to filter by mood");
            return new ArrayList<>();
        }
        
        String moodLowerCase = mood.toLowerCase().trim();
        List<Movie> filteredMovies = allMovies.stream()
                .filter(movie -> movie.getMoodTags() != null && 
                        movie.getMoodTags().stream()
                                .anyMatch(tag -> tag.toLowerCase().equals(moodLowerCase)))
                .collect(Collectors.toList());
        
        log.info("Found {} movies for mood: {}", filteredMovies.size(), mood);
        return filteredMovies;
    }

    /**
     * Map Contentstack Entry to Movie object using Gson
     * @param entry The Contentstack entry to map
     * @return Movie object, or null if mapping fails
     */
    private Movie mapEntryToMovie(Entry entry) {
        try {
            if (entry == null) {
                log.warn("Entry is null, cannot map to Movie");
                return null;
            }
            
            JSONObject json = entry.toJSON();
            if (json == null) {
                log.warn("Entry JSON is null for uid: {}", entry.getUid());
                return null;
            }
            
            Gson gson = new Gson();
            Movie movie = gson.fromJson(json.toString(), Movie.class);
            
            if (movie == null) {
                log.warn("Gson returned null for entry: {}", entry.getUid());
                return null;
            }
            
            log.debug("Successfully mapped movie: {} (uid: {})", movie.getTitle(), movie.getUid());
            return movie;
            
        } catch (Exception e) {
            log.error("Exception while mapping entry to movie: {}", 
                    entry != null ? entry.getUid() : "unknown", e);
            return null;
        }
    }

}

