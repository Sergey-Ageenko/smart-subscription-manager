package com.ssm.auth_service.utils;

import com.ssm.auth_service.model.constant.ApiConstants;
import com.ssm.auth_service.model.constant.ApiErrorMessage;
import com.ssm.common.exception.UnauthorizedException;
import org.springframework.http.ResponseCookie;

import java.time.Duration;

public class ApiUtils {

    private static final int tokenBeginIndex = 7;
    private static final String authPath = "/api/v1/auth";
    private static final int maxCookieAge = 30;

    public static ResponseCookie getCookieWithRefreshToken (String refreshToken){
        return ResponseCookie.from(
                        ApiConstants.REFRESH_TOKEN,
                        refreshToken)
                .httpOnly(true)
                .secure(true)
                .path(authPath)
                .maxAge(Duration.ofDays(maxCookieAge))
                .build();
    }

    public static String extractAccessToken(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith(ApiConstants.PREFIX_BEARER)) {
            throw new UnauthorizedException(ApiErrorMessage.INVALID_AUTHORIZATION_HEADER.getMessage());
        }
        return authorizationHeader.substring(tokenBeginIndex);
    }
}
