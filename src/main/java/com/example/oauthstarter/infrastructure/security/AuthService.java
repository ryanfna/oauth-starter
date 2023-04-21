package com.example.oauthstarter.infrastructure.security;

import com.example.oauthstarter.application.dto.auth.LoginInfoDto;
import com.example.oauthstarter.application.dto.auth.RefreshTokenDto;
import com.example.oauthstarter.application.dto.auth.UserLoginDto;
import com.example.oauthstarter.domain.service.AuthUserService;
import com.example.oauthstarter.infrastructure.utils.TokenProvider;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {
    private final AuthUserService userService;
    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;


    public LoginInfoDto login(UserLoginDto dto) {
        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.email(), dto.password())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        var accessToken = tokenProvider.createToken(authentication);
        var refreshToken = tokenProvider.createRefreshToken(authentication);
        return LoginInfoDto.of(accessToken, refreshToken);
    }

    public LoginInfoDto refresh(RefreshTokenDto dto) {
        var authentication = getAuthentication(dto.refreshToken());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        var accessToken = tokenProvider.createToken(authentication);
        var newRefreshToken = tokenProvider.createRefreshToken(authentication);
        return LoginInfoDto.of(accessToken, newRefreshToken);
    }

    private Authentication getAuthentication(String refreshToken) {
        var email = tokenProvider.getUsernameFromToken(refreshToken);
        var userDetails = userService.loadUserByUsername(email);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

}
