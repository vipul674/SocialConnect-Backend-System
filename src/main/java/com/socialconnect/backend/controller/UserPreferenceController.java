package com.socialconnect.backend.controller;

import com.socialconnect.backend.dto.preference.UserPreferenceRequest;
import com.socialconnect.backend.dto.preference.UserPreferenceResponse;
import com.socialconnect.backend.service.UserPreferenceService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users/{userId}/preferences")
public class UserPreferenceController {

    private final UserPreferenceService preferenceService;

    public UserPreferenceController(UserPreferenceService preferenceService) {
        this.preferenceService = preferenceService;
    }

    @PutMapping
    public ResponseEntity<UserPreferenceResponse> upsertPreference(@PathVariable Long userId,
                                                                   @Valid @RequestBody UserPreferenceRequest request) {
        return ResponseEntity.ok(preferenceService.upsertPreference(userId, request));
    }

    @GetMapping
    public ResponseEntity<UserPreferenceResponse> getPreference(@PathVariable Long userId) {
        return ResponseEntity.ok(preferenceService.getPreferenceByUserId(userId));
    }
}
