package com.ssm.auth.service.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ssm.auth.service.model.request.LoginRequest;
import com.ssm.auth.service.model.request.RegisterRequest;
import com.ssm.auth.service.model.response.TokenResponse;
import jakarta.validation.constraints.NotNull;

public interface AuthService {
    TokenResponse login(@NotNull LoginRequest request);
    TokenResponse register(@NotNull RegisterRequest request) throws JsonProcessingException;
    TokenResponse refresh(@NotNull String refreshToken);
    void logout(@NotNull String accessToken, @NotNull String refreshToken);
}
