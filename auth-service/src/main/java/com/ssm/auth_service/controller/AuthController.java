package com.ssm.auth_service.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ssm.auth_service.model.constants.ApiConstants;
import com.ssm.auth_service.model.request.LoginRequest;
import com.ssm.auth_service.model.request.RegisterRequest;
import com.ssm.auth_service.model.response.AuthResponse;
import com.ssm.auth_service.model.response.TokenResponse;
import com.ssm.auth_service.service.AuthService;
import com.ssm.auth_service.utils.ApiUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @RequestBody @Valid LoginRequest loginRequest)
    {
        TokenResponse tokenResponse = authService.login(loginRequest);
        AuthResponse authResponse = AuthResponse
                .createSuccessfulWithNewToken(tokenResponse.accessToken());
        ResponseCookie cookie = ApiUtils.getCookieWithRefreshToken(tokenResponse.refreshToken());
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
        ResponseCookie cookie = ApiUtils.getCookieWithRefreshToken(tokenResponse.refreshToken());
        return ResponseEntity.ok()
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
        ResponseCookie cookie = ApiUtils.getCookieWithRefreshToken(tokenResponse.refreshToken());
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(authResponse);
    }
}
