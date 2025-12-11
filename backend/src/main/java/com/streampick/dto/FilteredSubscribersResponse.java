package com.streampick.dto;

import lombok.Data;
import java.util.List;

/**
 * Response DTO containing filtered subscribers
 */
@Data
public class FilteredSubscribersResponse {
    private boolean success;
    private int totalMatching;
    private List<SubscriberInfo> subscribers;
    
    @Data
    public static class SubscriberInfo {
        private String name;
        private String email;
        private List<String> matchingMoods;
        
        public SubscriberInfo(String name, String email, List<String> matchingMoods) {
            this.name = name;
            this.email = email;
            this.matchingMoods = matchingMoods;
        }
    }
}

