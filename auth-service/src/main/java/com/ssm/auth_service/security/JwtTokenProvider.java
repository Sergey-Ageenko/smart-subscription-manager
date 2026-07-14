package com.ssm.auth_service.security;

import com.ssm.auth_service.model.constant.ApiConstants;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
