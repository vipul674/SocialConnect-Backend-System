package com.socialconnect.backend.dto.preference;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public class UserPreferenceRequest {

    @Min(value = 18, message = "Target minimum age must be at least 18")
    @Max(value = 100, message = "Target minimum age must be less than or equal to 100")
    private Integer targetMinAge;

    @Min(value = 18, message = "Target maximum age must be at least 18")
    @Max(value = 100, message = "Target maximum age must be less than or equal to 100")
    private Integer targetMaxAge;

    @Size(max = 120, message = "Preferred location can be at most 120 characters")
    private String preferredLocation;

    @Size(max = 500, message = "User interests can be at most 500 characters")
    private String userInterests;

    @Size(max = 500, message = "Preferred interests can be at most 500 characters")
    private String preferredInterests;

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
}
