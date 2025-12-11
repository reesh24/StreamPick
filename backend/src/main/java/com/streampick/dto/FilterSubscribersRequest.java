package com.streampick.dto;

import lombok.Data;
import java.util.List;

/**
 * Request DTO for filtering subscribers by mood tags
 */
@Data
public class FilterSubscribersRequest {
    private List<String> moodTags;
}

