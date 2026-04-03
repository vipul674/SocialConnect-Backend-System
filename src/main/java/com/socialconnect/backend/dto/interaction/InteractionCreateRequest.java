package com.socialconnect.backend.dto.interaction;

import com.socialconnect.backend.entity.InteractionType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class InteractionCreateRequest {

    @NotNull(message = "Actor user id is required")
    @Positive(message = "Actor user id must be positive")
    private Long actorUserId;

    @NotNull(message = "Target user id is required")
    @Positive(message = "Target user id must be positive")
    private Long targetUserId;

    @NotNull(message = "Interaction type is required")
    private InteractionType type;

    public Long getActorUserId() {
        return actorUserId;
    }

    public void setActorUserId(Long actorUserId) {
        this.actorUserId = actorUserId;
    }

    public Long getTargetUserId() {
        return targetUserId;
    }

    public void setTargetUserId(Long targetUserId) {
        this.targetUserId = targetUserId;
    }

    public InteractionType getType() {
        return type;
    }

    public void setType(InteractionType type) {
        this.type = type;
    }
}
