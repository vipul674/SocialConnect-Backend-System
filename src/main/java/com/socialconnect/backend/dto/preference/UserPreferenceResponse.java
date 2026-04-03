package com.socialconnect.backend.dto.preference;

import java.time.Instant;

public class UserPreferenceResponse {

    private Long id;
    private Long userId;
    private Integer targetMinAge;
    private Integer targetMaxAge;
    private String preferredLocation;
    private String userInterests;
    private String preferredInterests;
    private Instant createdAt;
    private Instant updatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getTargetMinAge() {
        return targetMinAge;
    }

    public void setTargetMinAge(Integer targetMinAge) {
        this.targetMinAge = targetMinAge;
    }

    public Integer getTargetMaxAge() {
        return targetMaxAge;
    }

    public void setTargetMaxAge(Integer targetMaxAge) {
        this.targetMaxAge = targetMaxAge;
    }

    public String getPreferredLocation() {
        return preferredLocation;
    }

    public void setPreferredLocation(String preferredLocation) {
        this.preferredLocation = preferredLocation;
    }

    public String getUserInterests() {
        return userInterests;
    }

    public void setUserInterests(String userInterests) {
        this.userInterests = userInterests;
    }

    public String getPreferredInterests() {
        return preferredInterests;
    }

    public void setPreferredInterests(String preferredInterests) {
        this.preferredInterests = preferredInterests;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
