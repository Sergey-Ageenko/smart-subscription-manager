package com.ssm.apigateway.security;

import com.ssm.apigateway.constant.ApiErrorMessage;
import com.ssm.common.exception.UnauthorizedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
public class JwtTokenProvider {

    private final SecretKey secretKey;
    private final String expectedIssuer;
    private final Set<String> expectedAudiences;

    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey,
                            @Value("${jwt.issuer}") String expectedIssuer,
                            @Value("${jwt.audiences}") Set<String> expectedAudiences
                            ) {
        this.secretKey = createSecretKey(secretKey);
        this.expectedIssuer = expectedIssuer;
        this.expectedAudiences = expectedAudiences;
    }

    public Claims extractAllClaims(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(secretKey)
                .requireIssuer(expectedIssuer)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        validateAudience(claims);
        return claims;
    }

    public String getUserId(Claims claims) {
        return claims.getSubject();
    }

    @SuppressWarnings("unchecked")
    public List<SimpleGrantedAuthority> getRoles(Claims claims) {
        List<String> roles = claims.get("userRoles", List.class);
        return roles.stream()
                .map(SimpleGrantedAuthority::new)
                .toList();
    }
    private void validateAudience(Claims claims) {
        Set<String> tokenAudiences = claims.getAudience();
        if (tokenAudiences == null || tokenAudiences.isEmpty()) {
            throw new UnauthorizedException(ApiErrorMessage.JWT_AUDIENCE_IS_MISSING.getMessage());
        }
        boolean valid = tokenAudiences.stream()
                .anyMatch(expectedAudiences::contains);
        if (!valid) {
            throw new UnauthorizedException(ApiErrorMessage.INVALID_AUDIENCE.getMessage());
        }
    }

    private SecretKey createSecretKey(String secretKey64) {
        byte[] decodedKey = Decoders.BASE64.decode(secretKey64);
        return Keys.hmacShaKeyFor(decodedKey);
    }
}
