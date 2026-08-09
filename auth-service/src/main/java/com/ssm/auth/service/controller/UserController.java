package com.ssm.auth.service.controller;

import com.ssm.auth.service.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    @GetMapping("/me")
    @PreAuthorize("hasRole('ADMIN')")
    public UserPrincipal me(
            @AuthenticationPrincipal UserPrincipal principal) {
        log.info(principal.getUsername());
        return principal;
    }

    @GetMapping("/test")
    public String test() {
        return "auth-service works";
    }
}