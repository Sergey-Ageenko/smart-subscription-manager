package com.ssm.auth_service.service;

import com.ssm.auth_service.security.JwtUserPrincipal;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public interface RefreshTokenService {
    String create(@NotNull JwtUserPrincipal principal);
    UUID validate(@NotNull String refreshToken);
    void delete(@NotNull String refreshToken);
}
