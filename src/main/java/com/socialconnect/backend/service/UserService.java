package com.socialconnect.backend.service;

import com.socialconnect.backend.dto.user.UserCreateRequest;
import com.socialconnect.backend.dto.user.UserResponse;
import com.socialconnect.backend.dto.user.UserUpdateRequest;
import com.socialconnect.backend.entity.User;
import com.socialconnect.backend.exception.ConflictException;
import com.socialconnect.backend.exception.ResourceNotFoundException;
import com.socialconnect.backend.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponse createUser(UserCreateRequest request) {
        if (userRepository.existsByUsername(request.getUsername().trim())) {
            throw new ConflictException("Username already exists");
        }
        if (userRepository.existsByEmail(request.getEmail().trim())) {
            throw new ConflictException("Email already exists");
        }

        User user = new User();
        user.setUsername(request.getUsername().trim());
        user.setEmail(request.getEmail().trim());
        user.setBio(trimToNull(request.getBio()));
        user.setProfilePictureUrl(trimToNull(request.getProfilePictureUrl()));

        return toResponse(userRepository.save(user));
    }

    @Transactional(readOnly = true)
    public UserResponse getUserById(Long id) {
        return toResponse(findUserById(id));
    }

    @Transactional(readOnly = true)
    public Page<UserResponse> getUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(this::toResponse);
    }

    public UserResponse updateUser(Long id, UserUpdateRequest request) {
        User user = findUserById(id);

        if (StringUtils.hasText(request.getUsername())) {
            String username = request.getUsername().trim();
            if (userRepository.existsByUsernameAndIdNot(username, id)) {
                throw new ConflictException("Username already exists");
            }
            user.setUsername(username);
        }

        if (StringUtils.hasText(request.getEmail())) {
            String email = request.getEmail().trim();
            if (userRepository.existsByEmailAndIdNot(email, id)) {
                throw new ConflictException("Email already exists");
            }
            user.setEmail(email);
        }

        if (request.getBio() != null) {
            user.setBio(trimToNull(request.getBio()));
        }

        if (request.getProfilePictureUrl() != null) {
            user.setProfilePictureUrl(trimToNull(request.getProfilePictureUrl()));
        }

        return toResponse(userRepository.save(user));
    }

    public UserResponse updateProfilePictureUrl(Long id, String profilePictureUrl) {
        User user = findUserById(id);
        user.setProfilePictureUrl(trimToNull(profilePictureUrl));
        return toResponse(userRepository.save(user));
    }

    public void deleteUser(Long id) {
        User user = findUserById(id);
        userRepository.delete(user);
    }

    private User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
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
