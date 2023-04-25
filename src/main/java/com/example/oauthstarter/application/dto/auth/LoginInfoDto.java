package com.example.oauthstarter.application.dto.auth;


public record LoginInfoDto(String accessToken, String refreshToken, String role) {
    public static LoginInfoDto of(String accessToken, String refreshToken, String role) {
        return new LoginInfoDto(accessToken, refreshToken, role);
    }
}
