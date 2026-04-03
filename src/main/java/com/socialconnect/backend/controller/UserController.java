package com.socialconnect.backend.controller;

import com.socialconnect.backend.dto.common.PageResponse;
import com.socialconnect.backend.dto.user.UserCreateRequest;
import com.socialconnect.backend.dto.user.UserResponse;
import com.socialconnect.backend.dto.user.UserUpdateRequest;
import com.socialconnect.backend.exception.BadRequestException;
import com.socialconnect.backend.service.UserService;
import com.socialconnect.backend.service.storage.StorageService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@Validated
public class UserController {

    private final UserService userService;
    private final StorageService storageService;

    public UserController(UserService userService, StorageService storageService) {
        this.userService = userService;
        this.storageService = storageService;
    }

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserCreateRequest request) {
        return ResponseEntity.status(201).body(userService.createUser(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping
    public ResponseEntity<PageResponse<UserResponse>> getUsers(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return ResponseEntity.ok(PageResponse.from(userService.getUsers(pageable)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long id,
                                                   @Valid @RequestBody UserUpdateRequest request) {
        return ResponseEntity.ok(userService.updateUser(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/{id}/profile-picture", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserResponse> uploadProfilePicture(@PathVariable Long id,
                                                             @RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            throw new BadRequestException("Profile picture file is empty");
        }

        String fileName = sanitizeFilename(file.getOriginalFilename());
        String objectKey = "users/" + id + "/profile/" + UUID.randomUUID() + "-" + fileName;
        storageService.upload(objectKey, file);
        String profilePictureUrl = storageService.getObjectUrl(objectKey);

        return ResponseEntity.ok(userService.updateProfilePictureUrl(id, profilePictureUrl));
    }

    @DeleteMapping("/{id}/profile-picture")
    public ResponseEntity<Void> deleteProfilePicture(@PathVariable Long id,
                                                     @RequestParam("key") String objectKey) {
        storageService.deleteObject(objectKey);
        userService.updateProfilePictureUrl(id, null);
        return ResponseEntity.noContent().build();
    }

    private String sanitizeFilename(String originalFileName) {
        if (!StringUtils.hasText(originalFileName)) {
            return "profile-image";
        }
        return originalFileName.trim().replaceAll("[^a-zA-Z0-9.\\-]", "_");
    }
}
