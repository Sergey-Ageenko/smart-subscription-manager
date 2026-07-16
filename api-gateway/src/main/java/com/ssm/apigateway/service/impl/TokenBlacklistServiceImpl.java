package com.ssm.apigateway.service.impl;

import com.ssm.apigateway.constant.ApiConstants;
import com.ssm.apigateway.service.TokenBlacklistService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenBlacklistServiceImpl implements TokenBlacklistService {

    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public boolean isBlacklisted(String token) {
        return redisTemplate.hasKey(
                ApiConstants.PREFIX_BLACKLIST + token
        );
    }
}
