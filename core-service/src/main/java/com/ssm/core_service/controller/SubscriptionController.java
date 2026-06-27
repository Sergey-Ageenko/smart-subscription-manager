package com.ssm.core_service.controller;

import com.ssm.core_service.aspect.annotation.ApiLog;
import com.ssm.core_service.model.request.NewSubscriptionRequest;
import com.ssm.core_service.model.request.SubscriptionUpdateRequest;
import com.ssm.core_service.model.response.CoreResponse;
import com.ssm.core_service.model.response.SubscriptionResponse;
import com.ssm.core_service.security.JwtUserPrincipal;
import com.ssm.core_service.service.SubscriptionService;
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
@RequestMapping("/api/v1/subscriptions")
@ApiLog
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @GetMapping
    public ResponseEntity<CoreResponse<List<SubscriptionResponse>>> getAllSubscriptions(@AuthenticationPrincipal JwtUserPrincipal principal){

        return ResponseEntity.ok()
                .body(subscriptionService.getAllSubscriptions(principal.userId()));
    }

    @GetMapping("/{subscriptionId}")
    public ResponseEntity<CoreResponse<SubscriptionResponse>> getSubscription(@AuthenticationPrincipal JwtUserPrincipal principal, @PathVariable UUID subscriptionId){

        return ResponseEntity.ok()
                .body(subscriptionService.getSubscription(principal.userId(), subscriptionId));
    }

    @PostMapping
    public ResponseEntity<CoreResponse<SubscriptionResponse>> createSubscription(@AuthenticationPrincipal JwtUserPrincipal principal, @Valid @RequestBody NewSubscriptionRequest request){

        return ResponseEntity.ok()
                .body(subscriptionService.createSubscription(principal.userId(), request));
    }

    @PatchMapping("/{subscriptionId}/update")
    public ResponseEntity<CoreResponse<SubscriptionResponse>> updateSubscription(@AuthenticationPrincipal JwtUserPrincipal principal, @PathVariable UUID subscriptionId, @Valid @RequestBody SubscriptionUpdateRequest request){

        return ResponseEntity.ok()
                .body(subscriptionService.updateSubscription(principal.userId(), subscriptionId, request));
    }

    @PatchMapping("/{subscriptionId}/cancel")
    public ResponseEntity<CoreResponse<SubscriptionResponse>> cancelSubscription(@AuthenticationPrincipal JwtUserPrincipal principal, @PathVariable UUID subscriptionId){
        return ResponseEntity.ok()
                .body(subscriptionService.cancelSubscription(principal.userId(), subscriptionId));
    }
}

