package com.ssm.auth_service.service.impl;

import com.ssm.auth_service.model.constant.ApiConstants;
import com.ssm.auth_service.security.JwtTokenProvider;
import com.ssm.auth_service.service.TokenBlacklistService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenBlacklistServiceImpl implements TokenBlacklistService {

    private final RedisTemplate<String, String> redisTemplate;
    private final JwtTokenProvider tokenProvider;

    @Override
    public void blacklist(String accessToken) {
        tokenProvider.getRemainingLifetime(accessToken)
                .ifPresent(ttl -> redisTemplate.opsForValue().set(
                        ApiConstants.PREFIX_BLACKLIST + accessToken,
                        "",
                        ttl
                ));
    }
}
