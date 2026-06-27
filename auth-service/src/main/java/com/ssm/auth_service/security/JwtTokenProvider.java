package com.ssm.auth_service.security;

import com.ssm.auth_service.model.constants.ApiConstants;
import io.jsonwebtoken.Claims;
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
import java.util.function.Function;

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

    public String extractUserId(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public boolean isTokenValid(String token, JwtUserPrincipal jwtUserPrincipal) {
        final String userId = extractUserId(token);
        return (userId.equals(jwtUserPrincipal.getUserId().toString())) && !isTokenExpired(token);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
            return Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String generateToken(JwtUserPrincipal jwtUserPrincipal) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(ApiConstants.USER_ID, jwtUserPrincipal.getUserId());
        claims.put(ApiConstants.USER_ROLES, jwtUserPrincipal.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList());
        return generateToken(claims, jwtUserPrincipal);
    }

    private String generateToken(Map<String, Object> extraClaims, JwtUserPrincipal jwtUserPrincipal) {
        String userId = jwtUserPrincipal.getUserId().toString();
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
