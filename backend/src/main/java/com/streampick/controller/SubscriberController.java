package com.streampick.controller;

import com.streampick.dto.FilterSubscribersRequest;
import com.streampick.dto.FilteredSubscribersResponse;
import com.streampick.dto.Subscriber;
import com.streampick.service.SubscriberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST Controller for subscriber management
 */
@RestController
@RequestMapping("/api/subscribers")
@CrossOrigin(origins = "*")
public class SubscriberController {
    
    private static final Logger log = LoggerFactory.getLogger(SubscriberController.class);
    
    @Autowired
    private SubscriberService subscriberService;
    
    /**
     * Add a new subscriber to the Contentstack modular block
     * POST /api/subscribers/add
     *
     * @param subscriber Subscriber information
     * @return Success/failure response
     */
    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> addSubscriber(@RequestBody Subscriber subscriber) {
        log.info("Received subscription request for: {}", subscriber.getEmail());
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Validate input
            if (subscriber.getName() == null || subscriber.getName().trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "Name is required");
                return ResponseEntity.badRequest().body(response);
            }
            
            if (subscriber.getEmail() == null || subscriber.getEmail().trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "Email is required");
                return ResponseEntity.badRequest().body(response);
            }
            
            // Add subscriber
            boolean success = subscriberService.addSubscriber(
                subscriber.getName(),
                subscriber.getEmail(),
                subscriber.getPreferredMoods()
            );
            
            if (success) {
                response.put("success", true);
                response.put("message", "🎉 Successfully subscribed! You'll receive emails when new movies are published.");
                response.put("email", subscriber.getEmail());
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Failed to subscribe. Please try again.");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
            
        } catch (IllegalArgumentException e) {
            log.warn("Subscription failed for {}: {}", subscriber.getEmail(), e.getMessage());
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        } catch (Exception e) {
            log.error("Error adding subscriber: {}", subscriber.getEmail(), e);
            response.put("success", false);
            response.put("message", "Internal server error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Get subscriber count
     * GET /api/subscribers/count
     * 
     * @return Subscriber count
     */
    @GetMapping("/count")
    public ResponseEntity<Map<String, Object>> getSubscriberCount() {
        log.info("Fetching subscriber count");
        
        try {
            int count = subscriberService.getSubscriberCount();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("count", count);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Error fetching subscriber count", e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to fetch count");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Filter subscribers by mood tags - for Contentstack Automate
     * POST /api/subscribers/filter-by-moods
     * 
     * This endpoint is called by Automate to get a filtered list of subscribers
     * whose preferred moods match the published movie's mood tags
     * 
     * @param request Contains mood tags from the published movie
     * @return Filtered list of subscribers with matching mood preferences
     */
    @PostMapping("/filter-by-moods")
    public ResponseEntity<FilteredSubscribersResponse> filterSubscribersByMoods(
        @RequestBody FilterSubscribersRequest request) {
        
        log.info("Filtering subscribers by moods: {}", request.getMoodTags());
        
        FilteredSubscribersResponse response = new FilteredSubscribersResponse();
        
        try {
            // Get matching subscribers from service
            List<Subscriber> matchingSubscribers = 
                subscriberService.getSubscribersByMoods(request.getMoodTags());
            
            // Build response
            response.setSuccess(true);
            response.setTotalMatching(matchingSubscribers.size());
            response.setSubscribers(matchingSubscribers);
            
            log.info("Found {} matching subscribers", matchingSubscribers.size());
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Error filtering subscribers by moods", e);
            response.setSuccess(false);
            response.setTotalMatching(0);
            response.setSubscribers(List.of());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}

