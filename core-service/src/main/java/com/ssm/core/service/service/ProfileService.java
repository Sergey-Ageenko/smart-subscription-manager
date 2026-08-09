package com.ssm.core.service.service;

import com.ssm.core.service.model.request.user.ProfileUpdateRequest;
import com.ssm.core.service.model.response.CoreResponse;
import com.ssm.core.service.model.response.ProfileResponse;
import com.ssm.common.event.UserRegisteredEvent;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public interface ProfileService {
    CoreResponse<ProfileResponse> getProfile(@NotNull UUID userId);
    void createProfile(@NotNull UserRegisteredEvent event);
    CoreResponse<ProfileResponse> updateProfile(@NotNull UUID userId, @NotNull ProfileUpdateRequest request);
}
