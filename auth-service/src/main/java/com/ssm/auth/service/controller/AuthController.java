package com.ssm.auth.service.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ssm.auth.service.model.constant.ApiConstants;
import com.ssm.auth.service.model.request.LoginRequest;
import com.ssm.auth.service.model.request.RegisterRequest;
import com.ssm.auth.service.model.response.AuthResponse;
import com.ssm.auth.service.model.response.TokenResponse;
import com.ssm.auth.service.service.AuthService;
import com.ssm.auth.service.utils.ApiUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;
    private final ApiUtils apiUtils;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @RequestBody @Valid LoginRequest loginRequest) {
        TokenResponse tokenResponse = authService.login(loginRequest);
        AuthResponse authResponse = AuthResponse
                .createSuccessfulWithNewToken(tokenResponse.accessToken());
        ResponseCookie cookie = apiUtils.getCookieWithRefreshToken(tokenResponse.refreshToken());
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(authResponse);

    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
            @RequestBody @Valid RegisterRequest registerRequest) throws JsonProcessingException {
        TokenResponse tokenResponse = authService.register(registerRequest);
        AuthResponse authResponse = AuthResponse
                .createSuccessfulWithNewUser(tokenResponse.accessToken());
        ResponseCookie cookie = apiUtils.getCookieWithRefreshToken(tokenResponse.refreshToken());
        return ResponseEntity.status(HttpStatus.CREATED)
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(authResponse);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(
            @CookieValue(ApiConstants.REFRESH_TOKEN) String refreshToken
    ) {
        TokenResponse tokenResponse = authService.refresh(refreshToken);
        AuthResponse authResponse = AuthResponse
                .createSuccessfulWithNewToken(tokenResponse.accessToken());
        ResponseCookie cookie = apiUtils.getCookieWithRefreshToken(tokenResponse.refreshToken());
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(authResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @CookieValue(ApiConstants.REFRESH_TOKEN) String refreshToken,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader
    ) {
        String accessToken = apiUtils.extractAccessToken(authorizationHeader);
        authService.logout(accessToken, refreshToken);
        ResponseCookie cookie = apiUtils.clearRefreshTokenCookie();
        return ResponseEntity.noContent()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();
    }
}
