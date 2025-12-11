package com.streampick.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * Movie domain model representing a movie entity
 * Using Lombok @Data to auto-generate getters, setters, toString, equals, and hashCode
 */
@Data
public class Movie {
    
    @SerializedName("uid")
    private String uid;

    @SerializedName("title")
    private String title;

    @SerializedName("year")
    private Integer year;
    
    @SerializedName("runtime")
    private Integer runtime;

    @SerializedName("rating")
    private Double rating; 

    @SerializedName("genre")
    private List<String> genre;

    @SerializedName("mood_tags")  // Contentstack uses snake_case
    private List<String> moodTags;

    @SerializedName("platforms")
    private List<String> platforms;

    @SerializedName("description")
    private String description;

    @SerializedName("ai_description")  // Contentstack uses snake_case
    private String aiDescription;
    
    @SerializedName("image")
    private Map<String, Object> image;  // Contentstack file object
    
    // Helper method to get poster URL
    public String getImageUrl() {
        if (image != null && image.containsKey("url")) {
            return (String) image.get("url");
        }
        return null;
    }
}

