package com.ssm.core_service.service;

import com.ssm.core_service.model.request.user.ProfileSubscriptionAddRequest;
import com.ssm.core_service.model.request.user.ProfileSubscriptionUpdateRequest;
import com.ssm.core_service.model.response.CoreResponse;
import com.ssm.core_service.model.response.ProfileSubscriptionResponse;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public interface ProfileSubscriptionService {
    CoreResponse<List<ProfileSubscriptionResponse>> getAllSubscriptions(@NotNull UUID userId);
    CoreResponse<ProfileSubscriptionResponse> getSubscription(@NotNull UUID userId,@NotNull UUID subId);
    CoreResponse<ProfileSubscriptionResponse> addSubscription(@NotNull UUID userId, @NotNull UUID subId, @NotNull ProfileSubscriptionAddRequest request);
    CoreResponse<ProfileSubscriptionResponse> updateSubscription(@NotNull UUID userId, @NotNull UUID subId, @NotNull ProfileSubscriptionUpdateRequest request);
    CoreResponse<ProfileSubscriptionResponse> cancelSubscription(@NotNull UUID userId, @NotNull UUID subId);
    CoreResponse<ProfileSubscriptionResponse> activateSubscription(@NotNull UUID userId, @NotNull UUID subId);
    CoreResponse<ProfileSubscriptionResponse> deleteSubscription(@NotNull UUID userId, @NotNull UUID subId);
}
