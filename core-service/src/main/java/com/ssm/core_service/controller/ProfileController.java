package com.ssm.core_service.controller;

import com.ssm.core_service.model.request.ProfileUpdateRequest;
import com.ssm.core_service.model.response.CoreResponse;
import com.ssm.core_service.model.response.ProfileResponse;
import com.ssm.core_service.security.JwtUserPrincipal;
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
    public ResponseEntity<CoreResponse<ProfileResponse>> getProfile(@AuthenticationPrincipal JwtUserPrincipal principal) {
        return ResponseEntity.ok()
                .body(profileService.getProfile(principal.userId()));
    }

    @PatchMapping("/me/update")
    public ResponseEntity<CoreResponse<ProfileResponse>> updateProfile(@AuthenticationPrincipal JwtUserPrincipal principal,
                                                                       @Valid @RequestBody ProfileUpdateRequest request) {
        return ResponseEntity.ok()
                .body(profileService.updateProfile(principal.userId(), request));
    }

    @GetMapping("/test/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> testAdminRole(@AuthenticationPrincipal JwtUserPrincipal principal) {
        return ResponseEntity.ok()
                .body("ADMIN ID: " + principal.userId());
    }

}
