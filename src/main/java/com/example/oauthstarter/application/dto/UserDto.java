package com.example.oauthstarter.application.dto;

public record UserDto(String username, String name, String email) {
    public static UserDto ofDefault() {
        return new UserDto("tinhtute", "Trong Tinh", "trongtinh@lig.vn");
    }

    public static UserDto of(String username, String name, String email) {
        return new UserDto(username, name, email);
    }
}
