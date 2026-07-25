package com.ssm.core_service.controller;

import com.ssm.core_service.model.request.admin.SubscriptionNewRequest;
import com.ssm.core_service.model.request.admin.SubscriptionUpdateRequest;
import com.ssm.core_service.model.response.CoreResponse;
import com.ssm.core_service.model.response.SubscriptionResponse;
import com.ssm.core_service.service.SubscriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/core/subscriptions")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @GetMapping
    public ResponseEntity<CoreResponse<List<SubscriptionResponse>>> getAllSubscriptions() {
        return ResponseEntity.ok()
                .body(subscriptionService.getAllSubscriptions());
    }

    @GetMapping("/{subscriptionId}")
    public ResponseEntity<CoreResponse<SubscriptionResponse>> getSubscription(@PathVariable UUID subscriptionId) {
        return ResponseEntity.ok()
                .body(subscriptionService.getSubscription(subscriptionId));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CoreResponse<SubscriptionResponse>> createSubscription(@Valid @RequestBody SubscriptionNewRequest request) {
        return ResponseEntity.ok()
                .body(subscriptionService.createSubscription(request));
    }

    @PatchMapping("/{subscriptionId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CoreResponse<SubscriptionResponse>> updateSubscription(@PathVariable UUID subscriptionId,
                                                                                 @Valid @RequestBody SubscriptionUpdateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(subscriptionService.updateSubscription(subscriptionId, request));
    }

    @DeleteMapping("/{subscriptionId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CoreResponse<SubscriptionResponse>> deleteSubscription(@PathVariable UUID subscriptionId) {
        return ResponseEntity.ok()
                .body(subscriptionService.deleteSubscription(subscriptionId));
    }
}

