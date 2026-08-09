package com.ssm.auth.service.utils;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties(prefix = "app.cookie")
public record CookieProperties(
        boolean secure,
        Duration maxAge
) {
}
