package com.example.oauthstarter.domain.constant;

public enum UserRole {
    USER,
    ADMIN,
    MODERATOR;

    public static UserRole ofDefault() {
        return USER;
    }

    public String getAuthority() {
        return "ROLE_" + this.name();
    }
}
