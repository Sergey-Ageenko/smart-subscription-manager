package com.ssm.auth.service.service;

import com.ssm.auth.service.security.UserPrincipal;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public interface RefreshTokenService {
    String create(@NotNull UserPrincipal principal);
    UUID validate(@NotNull String refreshToken);
    void delete(@NotNull String refreshToken);
}
