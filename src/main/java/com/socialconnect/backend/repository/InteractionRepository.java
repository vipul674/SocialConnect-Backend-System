package com.socialconnect.backend.repository;

import com.socialconnect.backend.entity.Interaction;
import com.socialconnect.backend.entity.InteractionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InteractionRepository extends JpaRepository<Interaction, Long> {

    Page<Interaction> findByActorUserIdOrTargetUserIdOrderByCreatedAtDesc(Long actorUserId, Long targetUserId, Pageable pageable);

    boolean existsByActorUserIdAndTargetUserIdAndType(Long actorUserId, Long targetUserId, InteractionType type);
}
