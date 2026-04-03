package com.socialconnect.backend.service;

import com.socialconnect.backend.dto.feed.ActivityFeedItemResponse;
import com.socialconnect.backend.entity.Interaction;
import com.socialconnect.backend.exception.ResourceNotFoundException;
import com.socialconnect.backend.repository.InteractionRepository;
import com.socialconnect.backend.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ActivityFeedService {

    private final InteractionRepository interactionRepository;
    private final UserRepository userRepository;

    public ActivityFeedService(InteractionRepository interactionRepository, UserRepository userRepository) {
        this.interactionRepository = interactionRepository;
        this.userRepository = userRepository;
    }

    public Page<ActivityFeedItemResponse> getUserFeed(Long userId, Pageable pageable) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }

        return interactionRepository.findByActorUserIdOrTargetUserIdOrderByCreatedAtDesc(userId, userId, pageable)
                .map(this::toResponse);
    }

    private ActivityFeedItemResponse toResponse(Interaction interaction) {
        ActivityFeedItemResponse response = new ActivityFeedItemResponse();
        response.setInteractionId(interaction.getId());
        response.setActorUserId(interaction.getActorUser().getId());
        response.setActorUsername(interaction.getActorUser().getUsername());
        response.setTargetUserId(interaction.getTargetUser().getId());
        response.setTargetUsername(interaction.getTargetUser().getUsername());
        response.setType(interaction.getType());
        response.setCreatedAt(interaction.getCreatedAt());
        return response;
    }
}
