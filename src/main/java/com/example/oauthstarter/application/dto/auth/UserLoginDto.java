package com.example.oauthstarter.application.dto.auth;

public record UserLoginDto(
        String email,
        String password
) {
}
