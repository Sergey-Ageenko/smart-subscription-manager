package com.ssm.core_service.service.impl;

import com.ssm.common.exception.DataExistException;
import com.ssm.common.exception.NotFoundException;
import com.ssm.core_service.model.constant.ApiErrorMessage;
import com.ssm.core_service.model.entity.Budget;
import com.ssm.core_service.model.entity.Profile;
import com.ssm.core_service.model.request.userRequest.ProfileUpdateRequest;
import com.ssm.core_service.model.response.CoreResponse;
import com.ssm.core_service.model.response.ProfileResponse;
import com.ssm.core_service.repository.BudgetRepository;
import com.ssm.core_service.repository.ProfileRepository;
import com.ssm.core_service.service.ProfileService;
import com.ssm.common.event.UserRegisteredEvent;
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

    @Override
    @Transactional(readOnly = true)
    public CoreResponse<ProfileResponse> getProfile(UUID profileId) {
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new NotFoundException(
                        ApiErrorMessage.USER_PROFILE_NOT_FOUND_BY_ID.getMessage(profileId)
                ));
        return CoreResponse.createSuccessful(
                createResponse(profile)
        );
    }

    @Override
    @Transactional
    public void createProfile(UserRegisteredEvent event) {
        if (profileRepository.existsByUserId(event.userId())) {
            throw new DataExistException(ApiErrorMessage.USER_PROFILE_IS_ALREADY_EXISTS.getMessage(event.userId()));
        }
        Profile savedProfile = profileRepository.save(
                Profile.builder()
                        .userId(event.userId())
                        .firstName(event.firstName())
                        .lastName(event.lastName())
                        .build());
        Budget budget = budgetRepository.save(
                Budget.builder()
                        .monthlyLimit(BigDecimal.ZERO)
                        .profile(savedProfile)
                        .build());
        log.info("Profile {} with budget created successfully. Budget id = {}", savedProfile.getId(), budget.getId());
    }

    @Override
    @Transactional
    public CoreResponse<ProfileResponse> updateProfile(UUID profileId, ProfileUpdateRequest request) {
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new NotFoundException(
                        ApiErrorMessage.USER_PROFILE_NOT_FOUND_BY_ID.getMessage(profileId)
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

    @Override
    public UUID getProfileId(UUID userId) {
        Profile profile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException(
                        ApiErrorMessage.USER_PROFILE_NOT_FOUND_BY_ID.getMessage(userId)
                ));
        return profile.getId();
    }

    private ProfileResponse createResponse(Profile profile){
        return new ProfileResponse(
                profile.getFirstName(),
                profile.getLastName()
        );
    }
}
