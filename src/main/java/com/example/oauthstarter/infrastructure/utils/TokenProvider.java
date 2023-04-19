package com.example.oauthstarter.infrastructure.utils;

import com.example.oauthstarter.domain.model.AuthUserDetails;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.Objects;


@Slf4j
@Component
@RequiredArgsConstructor
public class TokenProvider {
    @Value("${application.oauth2.tokenSecret}")
    private String tokenSecret;

    @Value("${application.oauth2.tokenExpiration}")
    private Long tokenExpiration;

    public String createToken(Authentication authentication) {
        AuthUserDetails authUserDetails = (AuthUserDetails) authentication.getPrincipal();
        return generate(authUserDetails.getUsername());
    }

    public String generate(String username) {
        Date now = new Date();
        Date expiration = new Date(System.currentTimeMillis() + tokenExpiration);
        return Jwts.builder()
                .setId(username)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(toSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    public String getUsernameFromJWT(String token) {
        Claims claims = makeJwtParser().parseClaimsJws(token).getBody();
        return claims.getId();
    }

    public boolean validate(String token) {
        if (Objects.isNull(token)) return false;
        try {
            makeJwtParser().parseClaimsJws(token);
            return true;
        } catch (SecurityException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty.");
        } catch (SignatureException e) {
            log.error("JWT signature is not match with locally.");
        }
        return false;
    }

    // Make JWT Parser
    private JwtParser makeJwtParser() {
        return Jwts.parserBuilder().setSigningKey(toSigningKey()).build();
    }

    // Make Sign Key with HMAC
    private Key toSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(tokenSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
