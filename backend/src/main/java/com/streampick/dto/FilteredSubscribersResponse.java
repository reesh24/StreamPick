package com.streampick.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Response DTO containing filtered subscribers
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FilteredSubscribersResponse {
    private boolean success;
    private int totalMatching;
    private List<Subscriber> subscribers;
}

