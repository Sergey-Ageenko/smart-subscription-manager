package com.ssm.core_service.controller;

import com.ssm.core_service.model.request.userRequest.ProfileUpdateRequest;
import com.ssm.core_service.model.response.CoreResponse;
import com.ssm.core_service.model.response.ProfileResponse;
import com.ssm.core_service.security.UserPrincipal;
import com.ssm.core_service.service.ProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/profiles")
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping("/me")
    public ResponseEntity<CoreResponse<ProfileResponse>> getProfile(@AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok()
                .body(profileService.getProfile(principal.profileId()));
    }

    @PatchMapping("/me")
    public ResponseEntity<CoreResponse<ProfileResponse>> updateProfile(@AuthenticationPrincipal UserPrincipal principal,
                                                                       @Valid @RequestBody ProfileUpdateRequest request) {
        return ResponseEntity.ok()
                .body(profileService.updateProfile(principal.profileId(), request));
    }

    @GetMapping("/test/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> testAdminRole(@AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok()
                .body("ADMIN ID: " + principal.profileId());
    }

}
