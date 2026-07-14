package com.ssm.apigateway.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class JwtTokenProvider {

    private final SecretKey secretKey;
    private final String expectedIssuer;
    private final String expectedAudience;

    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey,
                            @Value("${jwt.issuer}") String expectedIssuer,
                            @Value("${jwt.audience}") String expectedAudience
                            ) {
        this.secretKey = createSecretKey(secretKey);
        this.expectedIssuer = expectedIssuer;
        this.expectedAudience = expectedAudience;
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .requireIssuer(expectedIssuer)
                .requireAudience(expectedAudience)
                .build()
                .parseSignedClaims(token)
                .getPayload();
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

    private SecretKey createSecretKey(String secretKey64) {
        byte[] decodedKey = Decoders.BASE64.decode(secretKey64);
        return Keys.hmacShaKeyFor(decodedKey);
    }
}
