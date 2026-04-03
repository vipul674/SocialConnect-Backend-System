package com.socialconnect.backend.service;

import com.socialconnect.backend.dto.user.UserResponse;
import com.socialconnect.backend.entity.User;
import com.socialconnect.backend.entity.UserPreference;
import com.socialconnect.backend.exception.ResourceNotFoundException;
import com.socialconnect.backend.repository.UserPreferenceRepository;
import com.socialconnect.backend.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@Transactional(readOnly = true)
public class RecommendationService {

    private final UserRepository userRepository;
    private final UserPreferenceRepository preferenceRepository;

    public RecommendationService(UserRepository userRepository, UserPreferenceRepository preferenceRepository) {
        this.userRepository = userRepository;
        this.preferenceRepository = preferenceRepository;
    }

    public Page<UserResponse> getRecommendations(Long userId, Pageable pageable) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }

        UserPreference preference = preferenceRepository.findByUserId(userId).orElse(null);

        String preferredLocation = preference == null ? null : trimToNull(preference.getPreferredLocation());
        String interestToken = preference == null ? null : extractFirstInterestToken(preference.getPreferredInterests());

        return userRepository.findRecommendationCandidates(userId, preferredLocation, interestToken, pageable)
                .map(this::toResponse);
    }

    private String extractFirstInterestToken(String interests) {
        if (!StringUtils.hasText(interests)) {
            return null;
        }

        String[] tokens = interests.split(",");
        for (String token : tokens) {
            if (StringUtils.hasText(token)) {
                return token.trim();
            }
        }

        return null;
    }

    private String trimToNull(String value) {
        if (!StringUtils.hasText(value)) {
            return null;
        }
        return value.trim();
    }

    private UserResponse toResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setBio(user.getBio());
        response.setProfilePictureUrl(user.getProfilePictureUrl());
        response.setCreatedAt(user.getCreatedAt());
        response.setUpdatedAt(user.getUpdatedAt());
        return response;
    }
}
