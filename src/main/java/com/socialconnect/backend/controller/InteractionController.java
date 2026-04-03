package com.socialconnect.backend.controller;

import com.socialconnect.backend.dto.common.PageResponse;
import com.socialconnect.backend.dto.interaction.InteractionCreateRequest;
import com.socialconnect.backend.dto.interaction.InteractionResponse;
import com.socialconnect.backend.service.InteractionService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@Validated
public class InteractionController {

    private final InteractionService interactionService;

    public InteractionController(InteractionService interactionService) {
        this.interactionService = interactionService;
    }

    @PostMapping("/interactions")
    public ResponseEntity<InteractionResponse> createInteraction(@Valid @RequestBody InteractionCreateRequest request) {
        return ResponseEntity.status(201).body(interactionService.createInteraction(request));
    }

    @GetMapping("/users/{userId}/interactions")
    public ResponseEntity<PageResponse<InteractionResponse>> getInteractions(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return ResponseEntity.ok(PageResponse.from(interactionService.getInteractionsForUser(userId, pageable)));
    }
}
