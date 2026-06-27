package com.ssm.auth_service.service.impl;

import com.ssm.auth_service.model.constants.ApiErrorMessage;
import com.ssm.auth_service.model.constants.ApiConstants;
import com.ssm.auth_service.model.exception.UnauthorizedException;
import com.ssm.auth_service.security.JwtUserPrincipal;
import com.ssm.auth_service.service.RefreshTokenService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RedisTemplate<String, String> redisTemplate;

    @Value("${jwt.refresh-token-expiration}")
    private Long refreshTokenExpiration;

    @Override
    public String create(@NotNull JwtUserPrincipal principal) {

        String userId = principal.getUserId().toString();

        String oldToken = redisTemplate.opsForValue()
                .get(ApiConstants.PREFIX_USER_REFRESH + userId);

        if (oldToken != null) {
            redisTemplate.delete(ApiConstants.PREFIX_REFRESH + oldToken);
        }

        String token = UUID.randomUUID().toString();

        redisTemplate.opsForValue().set(
                ApiConstants.PREFIX_REFRESH + token,
                userId,
                Duration.ofMillis(refreshTokenExpiration)
        );

        redisTemplate.opsForValue().set(
                ApiConstants.PREFIX_USER_REFRESH + userId,
                token,
                Duration.ofMillis(refreshTokenExpiration)
        );

        return token;
    }

    @Override
    public UUID validate(String refreshToken) {

        String userId = redisTemplate.opsForValue()
                .get(ApiConstants.PREFIX_REFRESH + refreshToken);


        if (userId == null) {
            throw new UnauthorizedException(ApiErrorMessage.TOKEN_EXPIRED.getMessage());
        }

        return UUID.fromString(userId);
    }

    @Override
    public void delete(String refreshToken) {
        redisTemplate.delete(ApiConstants.PREFIX_REFRESH + refreshToken);
    }
}
