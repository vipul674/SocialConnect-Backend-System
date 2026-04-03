package com.socialconnect.backend.service;

import com.socialconnect.backend.dto.preference.UserPreferenceRequest;
import com.socialconnect.backend.dto.preference.UserPreferenceResponse;
import com.socialconnect.backend.entity.User;
import com.socialconnect.backend.entity.UserPreference;
import com.socialconnect.backend.exception.BadRequestException;
import com.socialconnect.backend.exception.ResourceNotFoundException;
import com.socialconnect.backend.repository.UserPreferenceRepository;
import com.socialconnect.backend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@Transactional
public class UserPreferenceService {

    private final UserPreferenceRepository preferenceRepository;
    private final UserRepository userRepository;

    public UserPreferenceService(UserPreferenceRepository preferenceRepository, UserRepository userRepository) {
        this.preferenceRepository = preferenceRepository;
        this.userRepository = userRepository;
    }

    public UserPreferenceResponse upsertPreference(Long userId, UserPreferenceRequest request) {
        validatePreferenceRange(request);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        UserPreference preference = preferenceRepository.findByUserId(userId)
                .orElseGet(UserPreference::new);

        preference.setUser(user);
        preference.setTargetMinAge(request.getTargetMinAge());
        preference.setTargetMaxAge(request.getTargetMaxAge());
        preference.setPreferredLocation(trimToNull(request.getPreferredLocation()));
        preference.setUserInterests(trimToNull(request.getUserInterests()));
        preference.setPreferredInterests(trimToNull(request.getPreferredInterests()));

        return toResponse(preferenceRepository.save(preference));
    }

    @Transactional(readOnly = true)
    public UserPreferenceResponse getPreferenceByUserId(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }

        UserPreference preference = preferenceRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Preference not found for user id: " + userId));

        return toResponse(preference);
    }

    private void validatePreferenceRange(UserPreferenceRequest request) {
        if (request.getTargetMinAge() != null
                && request.getTargetMaxAge() != null
                && request.getTargetMinAge() > request.getTargetMaxAge()) {
            throw new BadRequestException("Target minimum age cannot be greater than target maximum age");
        }
    }

    private String trimToNull(String value) {
        if (!StringUtils.hasText(value)) {
            return null;
        }
        return value.trim();
    }

    private UserPreferenceResponse toResponse(UserPreference preference) {
        UserPreferenceResponse response = new UserPreferenceResponse();
        response.setId(preference.getId());
        response.setUserId(preference.getUser().getId());
        response.setTargetMinAge(preference.getTargetMinAge());
        response.setTargetMaxAge(preference.getTargetMaxAge());
        response.setPreferredLocation(preference.getPreferredLocation());
        response.setUserInterests(preference.getUserInterests());
        response.setPreferredInterests(preference.getPreferredInterests());
        response.setCreatedAt(preference.getCreatedAt());
        response.setUpdatedAt(preference.getUpdatedAt());
        return response;
    }
}
