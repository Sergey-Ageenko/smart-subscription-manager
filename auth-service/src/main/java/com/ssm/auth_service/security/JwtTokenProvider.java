package com.ssm.auth_service.security;

import com.ssm.auth_service.model.constant.ApiConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
public class JwtTokenProvider {


    private final SecretKey secretKey;
    private final Long accessTokenExpiration;

    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey,
                            @Value("${jwt.access-token-expiration}") Long accessTokenExpiration) {
        this.secretKey = createSecretKey(secretKey);
        this.accessTokenExpiration = accessTokenExpiration;
    }

    public String generateToken(UserPrincipal userPrincipal) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(ApiConstants.USER_ID, userPrincipal.getUserId());
        claims.put(ApiConstants.USER_ROLES, userPrincipal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList());
        return generateToken(claims, userPrincipal);
    }

    public Optional<Duration> getRemainingLifetime(String token) {
        try {
            Claims claims = extractAllClaims(token);
            long remaining = claims.getExpiration().getTime() - System.currentTimeMillis();
            if (remaining <= 0) {
                return Optional.empty();
            }
            return Optional.of(Duration.ofMillis(remaining));
        } catch (ExpiredJwtException e) {
            return Optional.empty();
        }
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private String generateToken(Map<String, Object> extraClaims, UserPrincipal userPrincipal) {
        String userId = userPrincipal.getUserId().toString();
        return Jwts.builder()
                .claims(extraClaims)
                .subject(userId)
                .issuer(ApiConstants.ISSUER)
                .audience()
                .add(ApiConstants.AUDIENCE)
                .and()
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + accessTokenExpiration))
                .signWith(secretKey, Jwts.SIG.HS256)
                .compact();
    }

    private SecretKey createSecretKey(String secretKey64) {
        byte[] decode64 = Decoders.BASE64.decode(secretKey64);
        return Keys.hmacShaKeyFor(decode64);
    }
}
