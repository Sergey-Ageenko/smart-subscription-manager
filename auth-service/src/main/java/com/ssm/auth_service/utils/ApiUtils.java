package com.ssm.auth_service.utils;

import com.ssm.auth_service.model.constants.ApiConstants;
import org.springframework.http.ResponseCookie;

import java.time.Duration;

public class ApiUtils {

    public static ResponseCookie getCookieWithRefreshToken (String refreshToken){
        return ResponseCookie.from(
                        ApiConstants.REFRESH_TOKEN,
                        refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/api/v1/auth/refresh")
                .maxAge(Duration.ofDays(30))
                .build();
    }
}
