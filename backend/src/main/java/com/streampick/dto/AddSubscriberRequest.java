package com.streampick.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO for adding a subscriber
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddSubscriberRequest {
    
    private String name;
    private String email;
    private List<String> preferredMoods;
}

