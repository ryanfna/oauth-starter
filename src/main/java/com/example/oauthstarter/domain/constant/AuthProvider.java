package com.example.oauthstarter.domain.constant;

public enum AuthProvider {
    LOCAL,
    FACEBOOK,
    GOOGLE,
    GITHUB;

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }

    public static AuthProvider ofDefault() {
        return LOCAL;
    }
}
