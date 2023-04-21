package com.example.oauthstarter.application.dto.auth;


public record LoginInfoDto(String accessToken, String refreshToken) {
    public static LoginInfoDto of(String accessToken, String refreshToken) {
        return new LoginInfoDto(accessToken, refreshToken);
    }
}
