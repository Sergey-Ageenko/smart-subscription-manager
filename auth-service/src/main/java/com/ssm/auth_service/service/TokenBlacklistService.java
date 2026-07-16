package com.ssm.auth_service.service;

public interface TokenBlacklistService {

    void blacklist(String accessToken);
}
