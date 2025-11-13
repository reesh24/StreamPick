package com.streampick.model;

import lombok.Data;

import java.util.List;

/**
 * Movie domain model representing a movie entity
 */
@Data
public class Movie {
    
    private String uid;
    private String title;
    private Integer year;
    private Integer runtime; // in minutes
    private Double rating; // 0-10 scale
    private List<String> genre;
    private List<String> moodTags;
    private List<String> platforms;
    private String description;
    private String aiDescription;
    private String posterUrl;
}

