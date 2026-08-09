package com.ssm.auth.service.service;

public interface TokenBlacklistService {

    void blacklist(String accessToken);
}
