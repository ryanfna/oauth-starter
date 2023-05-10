package com.example.oauthstarter.domain.service;

import com.example.oauthstarter.application.dto.auth.LoginInfoDto;
import com.example.oauthstarter.application.dto.auth.RefreshTokenDto;
import com.example.oauthstarter.application.dto.auth.UserLoginDto;
import com.example.oauthstarter.domain.model.AuthUserDetails;
import com.example.oauthstarter.infrastructure.utils.TokenProvider;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {
    private final AuthUserService userService;
    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;
    private final TokenService tokenService;


    public LoginInfoDto login(UserLoginDto dto) {
        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.email(), dto.password())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        var authUser = userService.loadUserByUsername(dto.email());
        return toLoginInfoDto(authUser);
    }

    public LoginInfoDto refresh(RefreshTokenDto dto) {
        var authUser = getAuthentication(dto.refreshToken());
        return toLoginInfoDto(authUser);
    }

    private AuthUserDetails getAuthentication(String refreshToken) {
        var email = tokenProvider.getUsernameFromToken(refreshToken);
        return userService.loadUserByUsername(email);
    }

    private LoginInfoDto toLoginInfoDto(AuthUserDetails authUser) {
        var accessToken = tokenProvider.createToken(authUser);
        var refreshToken = tokenProvider.createRefreshToken(authUser);
        tokenService.storeToken(refreshToken, authUser.getId());
        var role = String.join(",", authUser.getAuthorities()
                .stream()
                .map(Object::toString)
                .toArray(String[]::new));
        return LoginInfoDto.of(accessToken, refreshToken, role);
    }
}
