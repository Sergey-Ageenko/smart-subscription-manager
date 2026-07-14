package com.ssm.core_service.controller;

import com.ssm.core_service.model.request.userRequest.ProfileSubscriptionAddRequest;
import com.ssm.core_service.model.request.userRequest.ProfileSubscriptionUpdateRequest;
import com.ssm.core_service.model.response.CoreResponse;
import com.ssm.core_service.model.response.ProfileSubscriptionResponse;
import com.ssm.core_service.security.UserPrincipal;
import com.ssm.core_service.service.ProfileSubscriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/profile/subscriptions")
public class ProfileSubscriptionController {

    private final ProfileSubscriptionService profileSubscriptionService;

    @GetMapping
    public ResponseEntity<CoreResponse<List<ProfileSubscriptionResponse>>> getAllSubscriptions(@AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok()
                .body(profileSubscriptionService.getAllSubscriptions(principal.profileId()));
    }

    @GetMapping("/{subscriptionId}")
    public ResponseEntity<CoreResponse<ProfileSubscriptionResponse>> getSubscription(@AuthenticationPrincipal UserPrincipal principal,
                                                                                     @PathVariable UUID subscriptionId) {
        return ResponseEntity.ok()
                .body(profileSubscriptionService.getSubscription(principal.profileId(), subscriptionId));
    }

    @PostMapping("/{subscriptionId}")
    public ResponseEntity<CoreResponse<ProfileSubscriptionResponse>> addSubscription(@AuthenticationPrincipal UserPrincipal principal,
                                                                                     @PathVariable UUID subscriptionId,
                                                                                     @Valid @RequestBody ProfileSubscriptionAddRequest request) {
        return ResponseEntity.ok()
                .body(profileSubscriptionService.addSubscription(principal.profileId(), subscriptionId, request));
    }

    @PatchMapping("/{subscriptionId}/update")
    public ResponseEntity<CoreResponse<ProfileSubscriptionResponse>> updateSubscription(@AuthenticationPrincipal UserPrincipal principal,
                                                                                        @PathVariable UUID subscriptionId,
                                                                                        @Valid @RequestBody ProfileSubscriptionUpdateRequest request) {
        return ResponseEntity.ok()
                .body(profileSubscriptionService.updateSubscription(principal.profileId(), subscriptionId, request));
    }

    @PatchMapping("/{subscriptionId}/cancel")
    public ResponseEntity<CoreResponse<ProfileSubscriptionResponse>> cancelSubscription(@AuthenticationPrincipal UserPrincipal principal,
                                                                                 @PathVariable UUID subscriptionId) {
        return ResponseEntity.ok()
                .body(profileSubscriptionService.cancelSubscription(principal.profileId(), subscriptionId));
    }

    @DeleteMapping("/{subscriptionId}")
    public ResponseEntity<CoreResponse<ProfileSubscriptionResponse>> deleteSubscription(@AuthenticationPrincipal UserPrincipal principal,
                                                                                        @PathVariable UUID subscriptionId) {
        return ResponseEntity.ok()
                .body(profileSubscriptionService.deleteSubscription(principal.profileId(), subscriptionId));
    }

}
