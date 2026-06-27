package com.ssm.core_service.service.impl;

import com.ssm.core_service.exception.DataExistException;
import com.ssm.core_service.exception.InvalidDataException;
import com.ssm.core_service.exception.NotFoundException;
import com.ssm.core_service.model.constants.ApiErrorMessage;
import com.ssm.core_service.model.entity.Profile;
import com.ssm.core_service.model.entity.Subscription;
import com.ssm.core_service.model.enums.BillingPeriod;
import com.ssm.core_service.model.enums.SubscriptionStatus;
import com.ssm.core_service.model.request.NewSubscriptionRequest;
import com.ssm.core_service.model.request.SubscriptionUpdateRequest;
import com.ssm.core_service.model.response.CoreResponse;
import com.ssm.core_service.model.response.SubscriptionResponse;
import com.ssm.core_service.repository.ProfileRepository;
import com.ssm.core_service.repository.SubscriptionRepository;
import com.ssm.core_service.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final ProfileRepository profileRepository;

    @Override
    @Transactional(readOnly = true)
    public CoreResponse<List<SubscriptionResponse>> getAllSubscriptions(UUID profileId) {

        List<SubscriptionResponse> responses = subscriptionRepository
                .findAllByProfile_Id(profileId)
                .stream()
                .map(this::toResponse)
                .toList();

        return CoreResponse.createSuccessful(responses);
    }

    @Override
    @Transactional(readOnly = true)
    public CoreResponse<SubscriptionResponse> getSubscription(UUID profileId, UUID subId) {

        Subscription subscription = subscriptionRepository.findByIdAndProfile_Id(subId, profileId)
                .orElseThrow(() -> new NotFoundException(
                        ApiErrorMessage.USER_SUBSCRIPTION_NOT_FOUND_BY_ID.getMessage(subId)
                ));
        return CoreResponse.createSuccessful(toResponse(subscription));
    }

    @Override
    @Transactional
    public CoreResponse<SubscriptionResponse> createSubscription(UUID profileId, NewSubscriptionRequest request) {

        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new NotFoundException(
                        ApiErrorMessage.USER_PROFILE_NOT_FOUND_BY_ID.getMessage(profileId)
                ));

        if (subscriptionRepository.existsByName(request.name())){
            throw new DataExistException(ApiErrorMessage.SUBSCRIPTION_IS_ALREADY_EXISTS.getMessage(request.name()));
        }

        Subscription savedSubscription = subscriptionRepository.save(Subscription.builder()
                .name(request.name())
                .price(request.price())
                .category(request.category())
                .billingPeriod(request.billingPeriod())
                .status(SubscriptionStatus.ACTIVE)
                .nextPaymentDate(calculateNextPaymentDate(LocalDate.now(), request.billingPeriod()))
                .profile(profile)
                .build());

        log.info("Subscription {} created successfully.", savedSubscription.getId());

        return CoreResponse.createSuccessful(toResponse(savedSubscription));
    }

    @Override
    @Transactional
    public CoreResponse<SubscriptionResponse> updateSubscription(UUID profileId, UUID subId, SubscriptionUpdateRequest request) {

        Subscription subscription = subscriptionRepository.findByIdAndProfile_Id(subId, profileId)
                .orElseThrow(() -> new NotFoundException(
                        ApiErrorMessage.USER_SUBSCRIPTION_NOT_FOUND_BY_ID.getMessage(subId)
                ));

        if (StringUtils.hasText(request.name())) {
            subscription.setName(request.name());
        }

        if (request.price() != null && request.price().compareTo(BigDecimal.ZERO) > 0) {
            subscription.setPrice(request.price());
        }

        if (StringUtils.hasText(String.valueOf(request.category()))) {
            subscription.setCategory(request.category());
        }

        if (StringUtils.hasText(String.valueOf(request.billingPeriod()))) {
            subscription.setBillingPeriod(request.billingPeriod());
            subscription.setNextPaymentDate(calculateNextPaymentDate(LocalDate.now(), request.billingPeriod()));
        }

        log.info("Subscription {} updated successfully.", subscription.getId());

        return CoreResponse.createSuccessful(toResponse(subscription));
    }

    @Override
    @Transactional
    public CoreResponse<SubscriptionResponse> cancelSubscription(UUID profileId, UUID subId) {

        Subscription cancelledSubscription = subscriptionRepository.findByIdAndProfile_Id(subId, profileId)
                .orElseThrow(() -> new NotFoundException(
                        ApiErrorMessage.USER_SUBSCRIPTION_NOT_FOUND_BY_ID.getMessage(subId)
                ));
        cancelledSubscription.setStatus(SubscriptionStatus.CANCELLED);

        log.info("Subscription {} deleted successfully.", cancelledSubscription.getId());

        return CoreResponse.createSuccessful(toResponse(cancelledSubscription));
    }

    private SubscriptionResponse toResponse(Subscription subscription) {

        return new SubscriptionResponse(
                subscription.getId(),
                subscription.getName(),
                subscription.getPrice(),
                subscription.getCategory(),
                subscription.getBillingPeriod(),
                subscription.getStatus(),
                subscription.getNextPaymentDate()
        );
    }

    private LocalDate calculateNextPaymentDate(LocalDate baseDate, BillingPeriod period){

        if (baseDate == null || period == null){
            throw new InvalidDataException("Base date and billing period must not be null");
        }

        return switch (period){
            case WEEKLY -> baseDate.plusWeeks(1);
            case MONTHLY -> baseDate.plusMonths(1);
            case YEARLY -> baseDate.plusYears(1);
        };
    }
}
