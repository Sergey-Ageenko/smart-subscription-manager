package com.ssm.core_service.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ssm.core_service.model.request.NewSubscriptionRequest;
import com.ssm.core_service.model.request.SubscriptionUpdateRequest;
import com.ssm.core_service.model.response.CoreResponse;
import com.ssm.core_service.model.response.SubscriptionResponse;

import java.util.List;
import java.util.UUID;

public interface SubscriptionService {
    CoreResponse<List<SubscriptionResponse>> getAllSubscriptions(UUID profileId);
    CoreResponse<SubscriptionResponse> getSubscription(UUID profileId, UUID subId);
    CoreResponse<SubscriptionResponse> createSubscription(UUID profileId, NewSubscriptionRequest request) throws JsonProcessingException;
    CoreResponse<SubscriptionResponse> updateSubscription(UUID profileId, UUID subId, SubscriptionUpdateRequest request) throws JsonProcessingException;
    CoreResponse<SubscriptionResponse> cancelSubscription(UUID profileId, UUID subId) throws JsonProcessingException;
}
