package com.socialconnect.backend.controller;

import com.socialconnect.backend.dto.common.PageResponse;
import com.socialconnect.backend.dto.feed.ActivityFeedItemResponse;
import com.socialconnect.backend.dto.user.UserResponse;
import com.socialconnect.backend.service.ActivityFeedService;
import com.socialconnect.backend.service.RecommendationService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users/{userId}")
@Validated
public class ActivityFeedController {

    private final ActivityFeedService activityFeedService;
    private final RecommendationService recommendationService;

    public ActivityFeedController(ActivityFeedService activityFeedService,
                                  RecommendationService recommendationService) {
        this.activityFeedService = activityFeedService;
        this.recommendationService = recommendationService;
    }

    @GetMapping("/feed")
    public ResponseEntity<PageResponse<ActivityFeedItemResponse>> getFeed(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return ResponseEntity.ok(PageResponse.from(activityFeedService.getUserFeed(userId, pageable)));
    }

    @GetMapping("/recommendations")
    public ResponseEntity<PageResponse<UserResponse>> getRecommendations(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return ResponseEntity.ok(PageResponse.from(recommendationService.getRecommendations(userId, pageable)));
    }
}
