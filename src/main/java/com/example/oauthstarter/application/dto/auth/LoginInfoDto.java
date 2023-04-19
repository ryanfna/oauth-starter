package com.example.oauthstarter.application.dto.auth;

public record LoginInfoDto(String token) {
    public static LoginInfoDto of(String token) {
        return new LoginInfoDto(token);
    }
}
