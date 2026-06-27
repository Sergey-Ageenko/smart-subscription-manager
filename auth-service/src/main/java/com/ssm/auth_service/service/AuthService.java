package com.ssm.auth_service.service;

import com.ssm.auth_service.model.request.LoginRequest;
import com.ssm.auth_service.model.request.RegisterRequest;
import com.ssm.auth_service.model.response.TokenResponse;
import jakarta.validation.constraints.NotNull;

public interface AuthService {
    TokenResponse login(@NotNull LoginRequest request);
    TokenResponse register(@NotNull RegisterRequest request);
    TokenResponse refresh(@NotNull String refreshToken);

}
