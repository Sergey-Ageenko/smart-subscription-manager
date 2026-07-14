package com.ssm.auth_service.service;

import com.ssm.auth_service.security.UserPrincipal;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public interface RefreshTokenService {
    String create(@NotNull UserPrincipal principal);
    UUID validate(@NotNull String refreshToken);
    void delete(@NotNull String refreshToken);
}
