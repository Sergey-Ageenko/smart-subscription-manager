package com.ssm.core_service.service.impl;

import com.ssm.common.exception.DataExistException;
import com.ssm.common.exception.NotFoundException;
import com.ssm.core_service.model.constant.ApiErrorMessage;
import com.ssm.core_service.model.entity.Subscription;
import com.ssm.core_service.model.request.adminRequest.SubscriptionNewRequest;
import com.ssm.core_service.model.request.adminRequest.SubscriptionUpdateRequest;
import com.ssm.core_service.model.response.CoreResponse;
import com.ssm.core_service.model.response.SubscriptionResponse;
import com.ssm.core_service.repository.SubscriptionRepository;
import com.ssm.core_service.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;

    @Override
    @Transactional(readOnly = true)
    public CoreResponse<List<SubscriptionResponse>> getAllSubscriptions() {
        List<SubscriptionResponse> responses = subscriptionRepository
                .findAll()
                .stream()
                .map(this::createResponse)
                .toList();
        return CoreResponse.createSuccessful(responses);
    }

    @Override
    @Transactional(readOnly = true)
    public CoreResponse<SubscriptionResponse> getSubscription(UUID subId) {
        Subscription subscription = subscriptionRepository.findById(subId)
                .orElseThrow(() -> new NotFoundException(
                        ApiErrorMessage.SUBSCRIPTION_NOT_FOUND_BY_ID.getMessage(subId)
                ));
        return CoreResponse.createSuccessful(createResponse(subscription));
    }

    @Override
    @Transactional
    public CoreResponse<SubscriptionResponse> createSubscription(SubscriptionNewRequest request) {
        if (subscriptionRepository.existsByName(request.name())) {
            throw new DataExistException(ApiErrorMessage.SUBSCRIPTION_IS_ALREADY_EXISTS.getMessage(request.name()));
        }
        Subscription savedSubscription = subscriptionRepository.save(Subscription.builder()
                .name(request.name())
                .category(request.category())
                .build());
        log.info("Subscription {} created successfully.", savedSubscription.getId());
        return CoreResponse.createSuccessful(createResponse(savedSubscription));
    }

    @Override
    @Transactional
    public CoreResponse<SubscriptionResponse> updateSubscription(UUID subId, SubscriptionUpdateRequest request) {
        Subscription subscription = subscriptionRepository.findById(subId)
                .orElseThrow(() -> new NotFoundException(
                        ApiErrorMessage.SUBSCRIPTION_NOT_FOUND_BY_ID.getMessage(subId)
                ));
        if (StringUtils.hasText(request.name())) {
            subscription.setName(request.name());
        }
        if (request.category() != null) {
            subscription.setCategory(request.category());
        }
        log.info("Subscription {} updated successfully.", subscription.getId());
        return CoreResponse.createSuccessful(createResponse(subscription));
    }

    @Override
    @Transactional
    public CoreResponse<SubscriptionResponse> deleteSubscription(UUID subId) {
        Subscription deletedSubscription = subscriptionRepository.findById(subId)
                .orElseThrow(() -> new NotFoundException(
                        ApiErrorMessage.SUBSCRIPTION_NOT_FOUND_BY_ID.getMessage(subId)
                ));
        subscriptionRepository.delete(deletedSubscription);
        log.info("Subscription {} deleted successfully.", deletedSubscription.getId());
        return CoreResponse.createSuccessful(createResponse(deletedSubscription));
    }

    private SubscriptionResponse createResponse(Subscription subscription) {
        return new SubscriptionResponse(
                subscription.getId(),
                subscription.getName(),
                subscription.getCategory()
        );
    }
}
