package com.streampick.util;

import java.util.*;

/**
 * Maps user-friendly mood labels to backend mood tags
 * Mirrors the Python MoodMapper in recommendation-service
 */
public class MoodMapper {
    
    private static final Map<String, String> MOOD_ALIASES = new HashMap<>();
    
    static {
        // Direct mappings
        MOOD_ALIASES.put("cozy", "cozy");
        MOOD_ALIASES.put("thrilling", "thrilling");
        MOOD_ALIASES.put("laugh", "laugh");
        MOOD_ALIASES.put("deep", "deep");
        MOOD_ALIASES.put("escape", "escape");
        MOOD_ALIASES.put("chill", "chill");
        
        // UI-friendly aliases
        MOOD_ALIASES.put("cozy & warm", "cozy");
        MOOD_ALIASES.put("cozy and warm", "cozy");
        MOOD_ALIASES.put("warm", "cozy");
        
        MOOD_ALIASES.put("edge of seat", "thrilling");
        MOOD_ALIASES.put("edge-of-seat", "thrilling");
        MOOD_ALIASES.put("thriller", "thrilling");
        MOOD_ALIASES.put("suspense", "thrilling");
        MOOD_ALIASES.put("intense", "thrilling");
        
        MOOD_ALIASES.put("need laughs", "laugh");
        MOOD_ALIASES.put("need-laughs", "laugh");
        MOOD_ALIASES.put("funny", "laugh");
        MOOD_ALIASES.put("comedy", "laugh");
        MOOD_ALIASES.put("humor", "laugh");
        
        MOOD_ALIASES.put("make me think", "deep");
        MOOD_ALIASES.put("make-me-think", "deep");
        MOOD_ALIASES.put("thoughtful", "deep");
        MOOD_ALIASES.put("intellectual", "deep");
        MOOD_ALIASES.put("profound", "deep");
        
        MOOD_ALIASES.put("pure escapism", "escape");
        MOOD_ALIASES.put("pure-escapism", "escape");
        MOOD_ALIASES.put("escapism", "escape");
        MOOD_ALIASES.put("adventure", "escape");
        
        MOOD_ALIASES.put("background vibe", "chill");
        MOOD_ALIASES.put("background-vibe", "chill");
        MOOD_ALIASES.put("background", "chill");
        MOOD_ALIASES.put("relaxing", "chill");
        MOOD_ALIASES.put("mellow", "chill");
    }
    
    /**
     * Normalize mood input to backend mood tag
     * 
     * @param moodInput User's mood input (e.g., "Cozy & Warm", "THRILLING")
     * @return Normalized backend mood tag (e.g., "cozy", "thrilling")
     */
    public static String normalizeMood(String moodInput) {
        if (moodInput == null || moodInput.trim().isEmpty()) {
            return null;
        }
        
        // Normalize: lowercase, strip whitespace
        String normalized = moodInput.toLowerCase().trim();
        
        // Try direct lookup
        return MOOD_ALIASES.getOrDefault(normalized, normalized);
    }
    
    /**
     * Normalize a list of mood inputs
     * 
     * @param moodInputs List of mood inputs
     * @return List of normalized mood tags
     */
    public static List<String> normalizeMoods(List<String> moodInputs) {
        if (moodInputs == null) {
            return new ArrayList<>();
        }
        
        List<String> normalized = new ArrayList<>();
        for (String mood : moodInputs) {
            String normalizedMood = normalizeMood(mood);
            if (normalizedMood != null && !normalizedMood.isEmpty()) {
                normalized.add(normalizedMood);
            }
        }
        return normalized;
    }
    
    /**
     * Check if two mood lists have any common moods (after normalization)
     * 
     * @param userMoods User's preferred moods (UI-friendly)
     * @param movieMoods Movie's mood tags (backend format)
     * @return true if there's at least one match
     */
    public static boolean hasMatchingMood(List<String> userMoods, List<String> movieMoods) {
        if (userMoods == null || userMoods.isEmpty() || 
            movieMoods == null || movieMoods.isEmpty()) {
            return false;
        }
        
        // Normalize both lists
        List<String> normalizedUserMoods = normalizeMoods(userMoods);
        List<String> normalizedMovieMoods = normalizeMoods(movieMoods);
        
        // Check for intersection
        for (String userMood : normalizedUserMoods) {
            if (normalizedMovieMoods.contains(userMood)) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Get matching moods between user preferences and movie tags
     * 
     * @param userMoods User's preferred moods (UI-friendly)
     * @param movieMoods Movie's mood tags (backend format)
     * @return List of matching normalized moods
     */
    public static List<String> getMatchingMoods(List<String> userMoods, List<String> movieMoods) {
        if (userMoods == null || movieMoods == null) {
            return new ArrayList<>();
        }
        
        List<String> normalizedUserMoods = normalizeMoods(userMoods);
        List<String> normalizedMovieMoods = normalizeMoods(movieMoods);
        
        List<String> matches = new ArrayList<>();
        for (String userMood : normalizedUserMoods) {
            if (normalizedMovieMoods.contains(userMood)) {
                matches.add(userMood);
            }
        }
        
        return matches;
    }
    
    /**
     * Get UI-friendly mood labels
     */
    public static List<String> getUIFriendlyMoods() {
        return Arrays.asList(
            "Cozy & Warm",
            "Edge of Seat",
            "Need Laughs",
            "Make Me Think",
            "Pure Escapism",
            "Background Vibe"
        );
    }
    
    /**
     * Get backend mood tags
     */
    public static List<String> getBackendMoods() {
        return Arrays.asList("cozy", "thrilling", "laugh", "deep", "escape", "chill");
    }
}

