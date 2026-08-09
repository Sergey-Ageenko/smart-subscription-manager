package com.ssm.auth_service.utils;

import com.ssm.auth_service.model.constant.ApiConstants;
import com.ssm.auth_service.model.constant.ApiErrorMessage;
import com.ssm.common.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class ApiUtils {

    private static final String AUTH_PATH = "/api/v1/auth";
    private final CookieProperties cookieProperties;

    public ResponseCookie getCookieWithRefreshToken (String refreshToken){
        return ResponseCookie.from(
                        ApiConstants.REFRESH_TOKEN,
                        refreshToken)
                .httpOnly(true)
                .secure(cookieProperties.secure())
                .path(AUTH_PATH)
                .maxAge(cookieProperties.maxAge())
                .build();
    }

    public ResponseCookie clearRefreshTokenCookie() {
        return ResponseCookie.from(
                        ApiConstants.REFRESH_TOKEN,
                        ""
                )
                .httpOnly(true)
                .secure(cookieProperties.secure())
                .path(AUTH_PATH)
                .maxAge(Duration.ZERO)
                .build();
    }

    public String extractAccessToken(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith(ApiConstants.PREFIX_BEARER)) {
            throw new UnauthorizedException(ApiErrorMessage.INVALID_AUTHORIZATION_HEADER.getMessage());
        }
        return authorizationHeader.substring(ApiConstants.PREFIX_BEARER.length());
    }
}
