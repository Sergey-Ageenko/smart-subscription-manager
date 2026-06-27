package com.ssm.core_service.service;

import com.ssm.core_service.model.request.ProfileUpdateRequest;
import com.ssm.core_service.model.response.CoreResponse;
import com.ssm.core_service.model.response.ProfileResponse;
import events.UserRegisteredEvent;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public interface ProfileService {
    CoreResponse<ProfileResponse> getProfile(@NotNull UUID userId);
    void createProfile(@NotNull UserRegisteredEvent event);
    CoreResponse<ProfileResponse> updateProfile(@NotNull UUID userIdId, @NotNull ProfileUpdateRequest request);
}
