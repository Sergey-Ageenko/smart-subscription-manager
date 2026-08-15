package com.ssm.core.service.service.impl;

import com.ssm.common.exception.DataExistException;
import com.ssm.common.exception.NotFoundException;
import com.ssm.core.service.factory.BudgetEventFactory;
import com.ssm.core.service.model.constant.ApiErrorMessage;
import com.ssm.core.service.model.entity.Budget;
import com.ssm.core.service.model.entity.OutboxEvent;
import com.ssm.core.service.model.entity.Profile;
import com.ssm.core.service.model.request.user.ProfileUpdateRequest;
import com.ssm.core.service.model.response.CoreResponse;
import com.ssm.core.service.model.response.ProfileResponse;
import com.ssm.core.service.repository.BudgetRepository;
import com.ssm.core.service.repository.OutboxRepository;
import com.ssm.core.service.repository.ProfileRepository;
import com.ssm.core.service.service.ProfileService;
import com.ssm.events.UserRegisteredEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final ProfileRepository profileRepository;
    private final BudgetRepository budgetRepository;
    private final OutboxRepository outboxRepository;
    private final BudgetEventFactory eventFactory;

    @Override
    @Transactional(readOnly = true)
    public CoreResponse<ProfileResponse> getProfile(UUID userId) {
        Profile profile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException(
                        ApiErrorMessage.USER_PROFILE_NOT_FOUND.getMessage(userId)
                ));
        return CoreResponse.createSuccessful(
                createResponse(profile)
        );
    }

    @Override
    @Transactional
    public void createProfile(UserRegisteredEvent event) {
        if (profileRepository.existsByUserId(event.getUserId())) {
            throw new DataExistException(ApiErrorMessage.USER_PROFILE_IS_ALREADY_EXISTS.getMessage(event.getUserId()));
        }
        Profile savedProfile = profileRepository.save(
                Profile.builder()
                        .userId(event.getUserId())
                        .firstName(event.getFirstName())
                        .lastName(event.getLastName())
                        .build());
        Budget budget = budgetRepository.save(
                Budget.builder()
                        .monthlyLimit(BigDecimal.ZERO)
                        .profile(savedProfile)
                        .build());
        OutboxEvent outboxEvent = eventFactory.updated(event.getUserId());
        outboxRepository.save(outboxEvent);
        log.info("Profile {} with budget created successfully. Budget id = {}", savedProfile.getId(), budget.getId());
    }

    @Override
    @Transactional
    public CoreResponse<ProfileResponse> updateProfile(UUID userId, ProfileUpdateRequest request) {
        Profile profile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException(
                        ApiErrorMessage.USER_PROFILE_NOT_FOUND.getMessage(userId)
                ));
        if (StringUtils.hasText(request.firstName())) {
            profile.setFirstName(request.firstName());
        }
        if (StringUtils.hasText(request.lastName())) {
            profile.setLastName(request.lastName());
        }
        log.info("Profile {} updated successfully.", profile.getId());
        return CoreResponse.createSuccessful(
                createResponse(profile)
        );
    }


    private ProfileResponse createResponse(Profile profile){
        return new ProfileResponse(
                profile.getFirstName(),
                profile.getLastName()
        );
    }
}
