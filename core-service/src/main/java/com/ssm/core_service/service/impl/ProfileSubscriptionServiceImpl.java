package com.ssm.core_service.service.impl;

import com.ssm.common.exception.DataExistException;
import com.ssm.common.exception.InvalidDataException;
import com.ssm.common.exception.NotFoundException;
import com.ssm.core_service.factory.SubscriptionEventFactory;
import com.ssm.core_service.model.constant.ApiErrorMessage;
import com.ssm.core_service.model.entity.OutboxEvent;
import com.ssm.core_service.model.entity.Profile;
import com.ssm.core_service.model.entity.Subscription;
import com.ssm.core_service.model.entity.profileSubscription.ProfileSubscription;
import com.ssm.core_service.model.entity.profileSubscription.ProfileSubscriptionId;
import com.ssm.core_service.model.enums.BillingPeriod;
import com.ssm.core_service.model.enums.SubscriptionStatus;
import com.ssm.core_service.model.request.userRequest.ProfileSubscriptionAddRequest;
import com.ssm.core_service.model.request.userRequest.ProfileSubscriptionUpdateRequest;
import com.ssm.core_service.model.response.CoreResponse;
import com.ssm.core_service.model.response.ProfileSubscriptionResponse;
import com.ssm.core_service.repository.OutboxRepository;
import com.ssm.core_service.repository.ProfileRepository;
import com.ssm.core_service.repository.ProfileSubscriptionRepository;
import com.ssm.core_service.repository.SubscriptionRepository;
import com.ssm.core_service.service.ProfileSubscriptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProfileSubscriptionServiceImpl implements ProfileSubscriptionService {

    private final ProfileSubscriptionRepository profileSubscriptionRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final ProfileRepository profileRepository;
    private final SubscriptionEventFactory subscriptionEventFactory;
    private final OutboxRepository outboxRepository;

    @Override
    @Transactional(readOnly = true)
    public CoreResponse<List<ProfileSubscriptionResponse>> getAllSubscriptions(UUID profileId) {
        List<ProfileSubscriptionResponse> responses = profileSubscriptionRepository
                .findAllByProfile_Id(profileId)
                .stream()
                .map(this::createResponse)
                .toList();
        return CoreResponse.createSuccessful(responses);
    }

    @Override
    @Transactional(readOnly = true)
    public CoreResponse<ProfileSubscriptionResponse> getSubscription(UUID profileId, UUID subId) {
        ProfileSubscription profileSubscription = profileSubscriptionRepository.findById(
                        ProfileSubscriptionId.builder()
                                .profileId(profileId)
                                .subscriptionId(subId)
                                .build()
                )
                .orElseThrow(() -> new NotFoundException(
                        ApiErrorMessage.USER_SUBSCRIPTION_NOT_FOUND_BY_ID.getMessage(profileId)
                ));
        return CoreResponse.createSuccessful(createResponse(profileSubscription));
    }

    @Override
    @Transactional
    public CoreResponse<ProfileSubscriptionResponse> addSubscription(UUID profileId, UUID subId, ProfileSubscriptionAddRequest request) {
        if (profileSubscriptionRepository.existsById_SubscriptionId(subId)){
            throw new DataExistException(ApiErrorMessage.USER_SUBSCRIPTION_IS_ALREADY_EXISTS.getMessage(subId));
        }
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new NotFoundException(
                        ApiErrorMessage.USER_PROFILE_NOT_FOUND_BY_ID.getMessage(profileId)
                ));
        Subscription subscription = subscriptionRepository.findById(subId)
                .orElseThrow(() -> new NotFoundException(
                        ApiErrorMessage.SUBSCRIPTION_NOT_FOUND_BY_ID.getMessage(subId)
                ));
        ProfileSubscription savedProfileSubscription = profileSubscriptionRepository.save(
                createProfileSubscription(profile, subscription,
                        request.price(),
                        request.billingPeriod())
        );
        OutboxEvent event = subscriptionEventFactory.created(savedProfileSubscription);
        outboxRepository.save(event);
        log.info("Subscription {} added successfully to profile {}.",
                savedProfileSubscription.getSubscription().getId(),
                savedProfileSubscription.getProfile().getId());
        return CoreResponse.createSuccessful(createResponse(savedProfileSubscription));
    }

    @Override
    @Transactional
    public CoreResponse<ProfileSubscriptionResponse> updateSubscription(UUID profileId, UUID subId, ProfileSubscriptionUpdateRequest request) {
        ProfileSubscription updatedProfileSubscription = profileSubscriptionRepository.findById_ProfileIdAndSubscriptionId(profileId, subId)
                .orElseThrow(() -> new NotFoundException(
                        ApiErrorMessage.USER_SUBSCRIPTION_NOT_FOUND_BY_ID.getMessage(profileId)
                ));
        boolean financialDataChanged = false;
        if (request.price() != null && request.price().compareTo(BigDecimal.ZERO) > 0 &&
                request.price().compareTo(updatedProfileSubscription.getPrice()) != 0) {
            updatedProfileSubscription.setPrice(request.price());
            financialDataChanged = true;
        }
        if (request.billingPeriod() != null) {
            updatedProfileSubscription.setBillingPeriod(request.billingPeriod());
            updatedProfileSubscription.setNextPaymentDate(calculateNextPaymentDate(LocalDate.now(), request.billingPeriod()));
            financialDataChanged = true;
        }
        if (financialDataChanged){
            OutboxEvent event = subscriptionEventFactory.updated(updatedProfileSubscription);
            outboxRepository.save(event);
        }
        log.info("Subscription {} updated successfully from profile {}.",
                updatedProfileSubscription.getSubscription().getId(),
                updatedProfileSubscription.getProfile().getId());
        return CoreResponse.createSuccessful(createResponse(updatedProfileSubscription));
    }

    @Override
    @Transactional
    public CoreResponse<ProfileSubscriptionResponse> cancelSubscription(UUID profileId, UUID subId) {
        ProfileSubscription cancelledProfileSubscription = profileSubscriptionRepository.findById_ProfileIdAndSubscriptionId(profileId, subId)
                .orElseThrow(() -> new NotFoundException(
                        ApiErrorMessage.USER_SUBSCRIPTION_NOT_FOUND_BY_ID.getMessage(profileId)
                ));
        if (cancelledProfileSubscription.getStatus() != SubscriptionStatus.ACTIVE) {
            throw new DataExistException(ApiErrorMessage.USER_SUBSCRIPTION_IS_ALREADY_CANCELLED.getMessage(subId));
        }
        cancelledProfileSubscription.setStatus(SubscriptionStatus.CANCELLED);
        OutboxEvent event = subscriptionEventFactory.cancelled(cancelledProfileSubscription);
        outboxRepository.save(event);
        log.info("Subscription {} cancelled successfully from profile {}.",
                cancelledProfileSubscription.getSubscription().getId(),
                cancelledProfileSubscription.getProfile().getId());
        return CoreResponse.createSuccessful(createResponse(cancelledProfileSubscription));
    }

    @Override
    @Transactional
    public CoreResponse<ProfileSubscriptionResponse> deleteSubscription(UUID profileId, UUID subId) {
        ProfileSubscription deletedProfileSubscription = profileSubscriptionRepository.findById_ProfileIdAndSubscriptionId(profileId, subId)
                .orElseThrow(() -> new NotFoundException(
                        ApiErrorMessage.USER_SUBSCRIPTION_NOT_FOUND_BY_ID.getMessage(profileId)
                ));
        profileSubscriptionRepository.delete(deletedProfileSubscription);
        log.info("Subscription {} deleted successfully from profile {}.",
                deletedProfileSubscription.getSubscription().getId(),
                deletedProfileSubscription.getProfile().getId());
        return CoreResponse.createSuccessful(createResponse(deletedProfileSubscription));
    }


    private ProfileSubscription createProfileSubscription(Profile profile,
                                                          Subscription subscription,
                                                          BigDecimal price,
                                                          BillingPeriod billingPeriod) {
        ProfileSubscriptionId profileSubscriptionId = ProfileSubscriptionId.builder()
                .profileId(profile.getId())
                .subscriptionId(subscription.getId())
                .build();
        return ProfileSubscription.builder()
                .id(profileSubscriptionId)
                .profile(profile)
                .subscription(subscription)
                .price(price)
                .status(SubscriptionStatus.ACTIVE)
                .billingPeriod(billingPeriod)
                .nextPaymentDate(calculateNextPaymentDate(LocalDate.now(), billingPeriod))
                .build();
    }

    private ProfileSubscriptionResponse createResponse(ProfileSubscription subscription) {
        return new ProfileSubscriptionResponse(
                subscription.getSubscription().getId(),
                subscription.getSubscription().getName(),
                subscription.getSubscription().getCategory(),
                subscription.getPrice(),
                subscription.getStatus(),
                subscription.getBillingPeriod(),
                subscription.getNextPaymentDate()
        );
    }

    private LocalDate calculateNextPaymentDate(LocalDate baseDate, BillingPeriod period) {
        if (baseDate == null || period == null) {
            throw new InvalidDataException("Base date and billing period must not be null");
        }
        return switch (period) {
            case WEEKLY -> baseDate.plusWeeks(1);
            case MONTHLY -> baseDate.plusMonths(1);
            case YEARLY -> baseDate.plusYears(1);
        };
    }
}
