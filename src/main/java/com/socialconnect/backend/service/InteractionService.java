package com.socialconnect.backend.service;

import com.socialconnect.backend.dto.interaction.InteractionCreateRequest;
import com.socialconnect.backend.dto.interaction.InteractionResponse;
import com.socialconnect.backend.entity.Interaction;
import com.socialconnect.backend.entity.InteractionType;
import com.socialconnect.backend.entity.User;
import com.socialconnect.backend.exception.BadRequestException;
import com.socialconnect.backend.exception.ResourceNotFoundException;
import com.socialconnect.backend.repository.InteractionRepository;
import com.socialconnect.backend.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class InteractionService {

    private final InteractionRepository interactionRepository;
    private final UserRepository userRepository;

    public InteractionService(InteractionRepository interactionRepository, UserRepository userRepository) {
        this.interactionRepository = interactionRepository;
        this.userRepository = userRepository;
    }

    public InteractionResponse createInteraction(InteractionCreateRequest request) {
        if (request.getActorUserId().equals(request.getTargetUserId())) {
            throw new BadRequestException("Actor and target users must be different");
        }

        User actorUser = userRepository.findById(request.getActorUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Actor user not found with id: " + request.getActorUserId()));

        User targetUser = userRepository.findById(request.getTargetUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Target user not found with id: " + request.getTargetUserId()));

        Interaction interaction = new Interaction();
        interaction.setActorUser(actorUser);
        interaction.setTargetUser(targetUser);
        interaction.setType(resolveInteractionType(request));

        return toResponse(interactionRepository.save(interaction));
    }

    @Transactional(readOnly = true)
    public Page<InteractionResponse> getInteractionsForUser(Long userId, Pageable pageable) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }

        return interactionRepository.findByActorUserIdOrTargetUserIdOrderByCreatedAtDesc(userId, userId, pageable)
                .map(this::toResponse);
    }

    private InteractionType resolveInteractionType(InteractionCreateRequest request) {
        if (request.getType() == InteractionType.LIKE) {
            boolean hasMutualLike = interactionRepository.existsByActorUserIdAndTargetUserIdAndType(
                    request.getTargetUserId(),
                    request.getActorUserId(),
                    InteractionType.LIKE
            );
            if (hasMutualLike) {
                return InteractionType.MATCH;
            }
        }
        return request.getType();
    }

    private InteractionResponse toResponse(Interaction interaction) {
        InteractionResponse response = new InteractionResponse();
        response.setId(interaction.getId());
        response.setActorUserId(interaction.getActorUser().getId());
        response.setTargetUserId(interaction.getTargetUser().getId());
        response.setType(interaction.getType());
        response.setCreatedAt(interaction.getCreatedAt());
        return response;
    }
}
