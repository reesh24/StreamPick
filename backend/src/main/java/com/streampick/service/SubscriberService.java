package com.streampick.service;

import com.contentstack.cms.Contentstack;
import com.contentstack.cms.stack.Stack;
import com.streampick.util.MoodMapper;
import okhttp3.ResponseBody;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Response;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Service for managing subscribers using Contentstack Management API
 */
@Service
public class SubscriberService {
    
    private static final Logger log = LoggerFactory.getLogger(SubscriberService.class);
    
    @Value("${contentstack.api.key}")
    private String apiKey;
    
    @Value("${contentstack.authtoken}")
    private String authToken;
    
    @Value("${contentstack.subscribers.entry.uid}")
    private String subscribersEntryUid;

    @Value("${contentstack.management.token}")
    private String managementToken;
    
    private Contentstack managementClient;
    
    /**
     * Initialize Management Client using Management Token
     */
    private Contentstack getManagementClient() {
        if (managementClient == null) {
            log.debug("Initializing Contentstack Management Client");
            log.debug("AuthToken present: {}", (authToken != null && !authToken.isEmpty()));
            log.debug("AuthToken length: {}", authToken != null ? authToken.length() : 0);
            
            managementClient = new Contentstack.Builder().build();
                
            log.info("Management client initialized successfully");
        }
        return managementClient;
    }
    
    /**
     * Add a new subscriber to the modular block
     * 
     * @param name Subscriber name
     * @param email Subscriber email
     * @param preferredMoods List of preferred moods
     * @return true if successful, false otherwise
     */
    public boolean addSubscriber(String name, String email, List<String> preferredMoods) {
        log.info("Adding subscriber: {}", email);
        log.debug("API Key: {}", apiKey != null ? "Present" : "Missing");
        log.debug("Subscribers Entry UID: {}", subscribersEntryUid);
        
        try {
            // Initialize Management API client
            Contentstack cms = getManagementClient();
            
            log.debug("Creating stack entry reference for apiKey: {}", apiKey);
            Stack stack = cms.stack(apiKey, managementToken);
            
            // 1. Get existing entry using Management SDK
            com.contentstack.cms.stack.Entry entry = stack.contentType("users")
                .entry(subscribersEntryUid);
            
            log.debug("Fetching entry...");
            
            Call<ResponseBody> fetchCall = entry.fetch();
            Response<ResponseBody> fetchResponse = fetchCall.execute();
            
            if (!fetchResponse.isSuccessful()) {
                log.error("Failed to fetch entry: {}", fetchResponse.errorBody().string());
                return false;
            }
            
            // Parse response
            String responseBody = fetchResponse.body().string();
            JSONObject responseJson = new JSONObject(responseBody);
            JSONObject entryData = responseJson.getJSONObject("entry");
            
            // 2. Get current user_details array
            JSONArray userDetails = entryData.has("user_details") 
                ? entryData.getJSONArray("user_details") 
                : new JSONArray();
            
            // 3. Check if email already exists
            for (int i = 0; i < userDetails.length(); i++) {
                JSONObject userBlock = userDetails.getJSONObject(i);
                if (userBlock.has("user")) {
                    JSONObject user = userBlock.getJSONObject("user");
                    if (user.has("email") && user.getString("email").equalsIgnoreCase(email)) {
                        log.warn("Email already subscribed: {}", email);
                        throw new RuntimeException("This email is already subscribed!");
                    }
                }
            }
            
            // 4. Create new user block (matching exact Contentstack structure)
            JSONObject newUserBlock = new JSONObject();
            JSONObject newUser = new JSONObject();
            newUser.put("name", name);
            newUser.put("email", email);
            newUser.put("preferred_moods", String.join(", ", preferredMoods));
            newUser.put("subscribed_date", LocalDate.now().format(DateTimeFormatter.ISO_DATE));
            
            newUserBlock.put("user", newUser);
            
            // 5. Add to array
            userDetails.put(newUserBlock);
            
            log.info("Adding new user to array. Total subscribers will be: {}", userDetails.length());
            
            // 6. Build update payload using org.json.simple (required by Management SDK)
            org.json.simple.JSONObject updatePayload = new org.json.simple.JSONObject();
            org.json.simple.JSONObject entryUpdate = new org.json.simple.JSONObject();
            
            // Convert org.json.JSONArray to org.json.simple.JSONArray and collect all emails
            org.json.simple.JSONArray userDetailsSimple = new org.json.simple.JSONArray();
            StringBuilder allEmailsBuilder = new StringBuilder();
            
            for (int i = 0; i < userDetails.length(); i++) {
                JSONObject userBlock = userDetails.getJSONObject(i);
                org.json.simple.JSONObject simpleBlock = new org.json.simple.JSONObject();
                
                if (userBlock.has("user")) {
                    JSONObject user = userBlock.getJSONObject("user");
                    org.json.simple.JSONObject simpleUser = new org.json.simple.JSONObject();
                    simpleUser.put("name", user.optString("name"));
                    simpleUser.put("email", user.optString("email"));
                    simpleUser.put("preferred_moods", user.optString("preferred_moods"));
                    simpleUser.put("subscribed_date", user.optString("subscribed_date"));
                    
                    simpleBlock.put("user", simpleUser);
                    
                    // Collect email for all_users field
                    if (i > 0) {
                        allEmailsBuilder.append(", ");
                    }
                    allEmailsBuilder.append(user.optString("email"));
                }
                
                userDetailsSimple.add(simpleBlock);
            }
            
            String allUsers = allEmailsBuilder.toString();
            log.debug("All users email list: {}", allUsers);
            
            entryUpdate.put("title", "Subscribers");
            entryUpdate.put("user_details", userDetailsSimple);
            entryUpdate.put("all_users", allUsers);
            updatePayload.put("entry", entryUpdate);
            
            log.debug("Update payload: {}", updatePayload.toJSONString());
            
            // Use Management SDK to update
            Call<ResponseBody> updateCall = entry.update(updatePayload);
            Response<ResponseBody> updateResponse = updateCall.execute();
            
            if (updateResponse.isSuccessful()) {
                log.info("Successfully added subscriber: {}", email);
                return true;
            } else {
                String errorBody = updateResponse.errorBody() != null 
                    ? updateResponse.errorBody().string() 
                    : "Unknown error";
                log.error("Failed to update entry: {} - {}", updateResponse.code(), errorBody);
                return false;
            }
            
        } catch (Exception e) {
            log.error("Error adding subscriber: {}", email, e);
            throw new RuntimeException(e.getMessage());
        }
    }
    
    
    /**
     * Get count of subscribers using Management SDK
     */
    public int getSubscriberCount() {
        try {
            Contentstack cms = getManagementClient();
            com.contentstack.cms.stack.Entry entry = cms.stack(apiKey)
                .contentType("subscribers")
                .entry(subscribersEntryUid);
            
            Call<ResponseBody> fetchCall = entry.fetch();
            Response<ResponseBody> fetchResponse = fetchCall.execute();
            
            if (fetchResponse.isSuccessful()) {
                String responseBody = fetchResponse.body().string();
                JSONObject responseJson = new JSONObject(responseBody);
                JSONObject entryData = responseJson.getJSONObject("entry");
                
                if (entryData.has("user_details")) {
                    JSONArray userDetails = entryData.getJSONArray("user_details");
                    return userDetails.length();
                }
            }
            
            return 0;
        } catch (Exception e) {
            log.error("Error getting subscriber count", e);
            return 0;
        }
    }
    
    /**
     * DTO for Subscriber data
     */
    public static class Subscriber {
        private final String name;
        private final String email;
        private final List<String> preferredMoods;
        
        public Subscriber(String name, String email, List<String> preferredMoods) {
            this.name = name;
            this.email = email;
            this.preferredMoods = preferredMoods;
        }
        
        public String getName() { return name; }
        public String getEmail() { return email; }
        public List<String> getPreferredMoods() { return preferredMoods; }
    }
    
    /**
     * Get subscribers whose preferred moods match any of the given movie moods
     * 
     * @param movieMoodTags List of mood tags from the published movie
     * @return List of matching subscribers
     */
    public List<Subscriber> getSubscribersByMoods(List<String> movieMoodTags) {
        log.info("Fetching subscribers for moods: {}", movieMoodTags);
        
        try {
            Contentstack cms = getManagementClient();
            com.contentstack.cms.stack.Entry entry = cms.stack(apiKey, managementToken)
                .contentType("users")
                .entry(subscribersEntryUid);
            
            Call<ResponseBody> fetchCall = entry.fetch();
            Response<ResponseBody> fetchResponse = fetchCall.execute();
            
            if (!fetchResponse.isSuccessful()) {
                log.error("Failed to fetch subscribers: {}", fetchResponse.errorBody().string());
                return List.of();
            }
            
            String responseBody = fetchResponse.body().string();
            JSONObject responseJson = new JSONObject(responseBody);
            JSONObject entryData = responseJson.getJSONObject("entry");
            
            if (!entryData.has("user_details")) {
                log.warn("No user_details found in subscribers entry");
                return List.of();
            }
            
            JSONArray userDetails = entryData.getJSONArray("user_details");
            List<Subscriber> matchingSubscribers = new java.util.ArrayList<>();
            
            // Filter subscribers by mood matching
            for (int i = 0; i < userDetails.length(); i++) {
                JSONObject userBlock = userDetails.getJSONObject(i);
                if (userBlock.has("user")) {
                    JSONObject user = userBlock.getJSONObject("user");
                    String name = user.optString("name");
                    String email = user.optString("email");
                    String preferredMoodsStr = user.optString("preferred_moods", "");
                    
                    // Parse preferred moods (stored as comma-separated string)
                    List<String> preferredMoods = List.of(preferredMoodsStr.split(",\\s*"));
                    
                    // Use MoodMapper to check if any preferred mood matches any movie mood tag
                    boolean hasMatch = MoodMapper.hasMatchingMood(preferredMoods, movieMoodTags);
                    
                    if (hasMatch) {
                        matchingSubscribers.add(new Subscriber(name, email, preferredMoods));
                        List<String> matchingMoods = MoodMapper.getMatchingMoods(preferredMoods, movieMoodTags);
                        log.debug("Subscriber {} matches with moods: {}", email, matchingMoods);
                    }
                }
            }
            
            log.info("Found {} matching subscribers out of {} total", 
                matchingSubscribers.size(), userDetails.length());
            
            return matchingSubscribers;
            
        } catch (Exception e) {
            log.error("Error fetching subscribers by moods", e);
            return List.of();
        }
    }
}

