package com.streampick.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO for Subscriber data - used for both requests and responses
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Subscriber {
    private String name;
    private String email;
    private List<String> preferredMoods;
}

