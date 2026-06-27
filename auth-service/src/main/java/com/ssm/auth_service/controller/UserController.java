package com.ssm.auth_service.controller;

import com.ssm.auth_service.security.JwtUserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    @GetMapping("/me")
    @PreAuthorize("hasRole('ADMIN')")
    public JwtUserPrincipal me(
            @AuthenticationPrincipal JwtUserPrincipal principal) {
        log.info(principal.getUsername());
        return principal;
    }

    @GetMapping("/test")
    public Object test(Authentication authentication) {
        return authentication;
    }

}