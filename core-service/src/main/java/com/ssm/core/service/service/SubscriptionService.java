package com.ssm.core.service.service;

import com.ssm.core.service.model.request.admin.SubscriptionNewRequest;
import com.ssm.core.service.model.request.admin.SubscriptionUpdateRequest;
import com.ssm.core.service.model.response.CoreResponse;
import com.ssm.core.service.model.response.SubscriptionResponse;

import java.util.List;
import java.util.UUID;

public interface SubscriptionService {
    CoreResponse<List<SubscriptionResponse>> getAllSubscriptions();
    CoreResponse<SubscriptionResponse> getSubscription(UUID subId);
    CoreResponse<SubscriptionResponse> createSubscription(SubscriptionNewRequest request);
    CoreResponse<SubscriptionResponse> updateSubscription(UUID subId, SubscriptionUpdateRequest request);
    CoreResponse<SubscriptionResponse> deleteSubscription(UUID subId);
}
