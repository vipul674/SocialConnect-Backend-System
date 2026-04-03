package com.socialconnect.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.time.Instant;

@Entity
@Table(
        name = "user_preferences",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_user_preferences_user_id", columnNames = "user_id")
        },
        indexes = {
                @Index(name = "idx_user_preferences_location", columnList = "preferred_location"),
                @Index(name = "idx_user_preferences_user_id", columnList = "user_id")
        }
)
public class UserPreference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "target_min_age")
    private Integer targetMinAge;

    @Column(name = "target_max_age")
    private Integer targetMaxAge;

    @Column(name = "preferred_location", length = 120)
    private String preferredLocation;

    @Column(name = "user_interests", length = 500)
    private String userInterests;

    @Column(name = "preferred_interests", length = 500)
    private String preferredInterests;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    @PrePersist
    public void onCreate() {
        Instant now = Instant.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = Instant.now();
    }
}
