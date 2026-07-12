package com.ssm.core_service.service;

import com.ssm.core_service.model.request.userRequest.ProfileSubscriptionAddRequest;
import com.ssm.core_service.model.request.userRequest.ProfileSubscriptionUpdateRequest;
import com.ssm.core_service.model.response.CoreResponse;
import com.ssm.core_service.model.response.ProfileSubscriptionResponse;

import java.util.List;
import java.util.UUID;

public interface ProfileSubscriptionService {
    CoreResponse<List<ProfileSubscriptionResponse>> getAllSubscriptions(UUID profileId);
    CoreResponse<ProfileSubscriptionResponse> getSubscription(UUID profileId,UUID subId);
    CoreResponse<ProfileSubscriptionResponse> addSubscription(UUID profileId, UUID subId, ProfileSubscriptionAddRequest request);
    CoreResponse<ProfileSubscriptionResponse> updateSubscription(UUID profileId, UUID subId, ProfileSubscriptionUpdateRequest request);
    CoreResponse<ProfileSubscriptionResponse> cancelSubscription(UUID profileId, UUID subId);
    CoreResponse<ProfileSubscriptionResponse> deleteSubscription(UUID profileId, UUID subId);
}
