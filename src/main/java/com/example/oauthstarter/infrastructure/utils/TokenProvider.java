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
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;


@Slf4j
@Component
@RequiredArgsConstructor
public class TokenProvider {
    @Value("${application.oauth2.tokenSecret}")
    private String tokenSecret;

    @Value("${application.oauth2.accessTokenExpiration}")
    private Long accessTokenExpiration;

    @Value("${application.oauth2.refreshTokenExpiration}")
    private Long refreshTokenExpiration;

    public String createToken(Authentication authentication) {
        AuthUserDetails authUserDetails = (AuthUserDetails) authentication.getPrincipal();
        return buildToken(new HashMap<>(), authUserDetails.getUsername(), accessTokenExpiration);
    }

    public String createRefreshToken(Authentication authentication) {
        AuthUserDetails authUserDetails = (AuthUserDetails) authentication.getPrincipal();
        return buildToken(new HashMap<>(), authUserDetails.getUsername(), refreshTokenExpiration);
    }

    private String buildToken(Map<String, Object> extraClaims, String username, Long expiration) {
        Date now = new Date();
        Date tokenExpiration = new Date(System.currentTimeMillis() + expiration);
        return Jwts.builder()
                .setClaims(extraClaims)
                .setId(username)
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(tokenExpiration)
                .signWith(toSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    public String getUsernameFromToken(String token) {
        return extractClaim(token, Claims::getId);
    }

    public boolean isValid(String token) {
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

    // get claims token
    private Claims getClaims(String token) {
        return makeJwtParser().parseClaimsJws(token).getBody();
    }

    // extract claims from token
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getClaims(token);
        return claimsResolver.apply(claims);
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
