package com.example.oauthstarter.domain.constant;

public enum AuthProvider {
    LOCAL,
    FACEBOOK,
    GOOGLE,
    GITHUB;

    public static AuthProvider ofDefault() {
        return LOCAL;
    }
}
