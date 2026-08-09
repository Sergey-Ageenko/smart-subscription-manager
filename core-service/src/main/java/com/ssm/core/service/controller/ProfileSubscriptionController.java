package com.ssm.core.service.controller;

import com.ssm.core.service.model.request.user.ProfileSubscriptionAddRequest;
import com.ssm.core.service.model.request.user.ProfileSubscriptionUpdateRequest;
import com.ssm.core.service.model.response.CoreResponse;
import com.ssm.core.service.model.response.ProfileSubscriptionResponse;
import com.ssm.core.service.security.UserPrincipal;
import com.ssm.core.service.service.ProfileSubscriptionService;
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
@RequestMapping("/api/v1/core/profile/subscriptions")
public class ProfileSubscriptionController {

    private final ProfileSubscriptionService profileSubscriptionService;

    @GetMapping
    public ResponseEntity<CoreResponse<List<ProfileSubscriptionResponse>>> getAllSubscriptions(@AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok()
                .body(profileSubscriptionService.getAllSubscriptions(principal.userId()));
    }

    @GetMapping("/{subscriptionId}")
    public ResponseEntity<CoreResponse<ProfileSubscriptionResponse>> getSubscription(@AuthenticationPrincipal UserPrincipal principal,
                                                                                     @PathVariable UUID subscriptionId) {
        return ResponseEntity.ok()
                .body(profileSubscriptionService.getSubscription(principal.userId(), subscriptionId));
    }

    @PostMapping("/{subscriptionId}")
    public ResponseEntity<CoreResponse<ProfileSubscriptionResponse>> addSubscription(@AuthenticationPrincipal UserPrincipal principal,
                                                                                     @PathVariable UUID subscriptionId,
                                                                                     @Valid @RequestBody ProfileSubscriptionAddRequest request) {
        return ResponseEntity.ok()
                .body(profileSubscriptionService.addSubscription(principal.userId(), subscriptionId, request));
    }

    @PatchMapping("/{subscriptionId}/update")
    public ResponseEntity<CoreResponse<ProfileSubscriptionResponse>> updateSubscription(@AuthenticationPrincipal UserPrincipal principal,
                                                                                        @PathVariable UUID subscriptionId,
                                                                                        @Valid @RequestBody ProfileSubscriptionUpdateRequest request) {
        return ResponseEntity.ok()
                .body(profileSubscriptionService.updateSubscription(principal.userId(), subscriptionId, request));
    }

    @PatchMapping("/{subscriptionId}/cancel")
    public ResponseEntity<CoreResponse<ProfileSubscriptionResponse>> cancelSubscription(@AuthenticationPrincipal UserPrincipal principal,
                                                                                        @PathVariable UUID subscriptionId) {
        return ResponseEntity.ok()
                .body(profileSubscriptionService.cancelSubscription(principal.userId(), subscriptionId));
    }

    @PatchMapping("/{subscriptionId}/activate")
    public ResponseEntity<CoreResponse<ProfileSubscriptionResponse>> activateSubscription(@AuthenticationPrincipal UserPrincipal principal,
                                                                                          @PathVariable UUID subscriptionId) {
        return ResponseEntity.ok()
                .body(profileSubscriptionService.activateSubscription(principal.userId(), subscriptionId));
    }

    @DeleteMapping("/{subscriptionId}")
    public ResponseEntity<CoreResponse<ProfileSubscriptionResponse>> deleteSubscription(@AuthenticationPrincipal UserPrincipal principal,
                                                                                        @PathVariable UUID subscriptionId) {
        return ResponseEntity.ok()
                .body(profileSubscriptionService.deleteSubscription(principal.userId(), subscriptionId));
    }

}
