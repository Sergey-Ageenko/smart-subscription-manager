package com.ssm.apigateway.service;

public interface TokenBlacklistService {
    boolean isBlacklisted(String token);
}
