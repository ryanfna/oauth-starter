package com.example.oauthstarter.application.dto;

import com.example.oauthstarter.domain.model.User;

public record UserDto(String name, String email, String imageUrl) {
    public static UserDto ofDefault() {
        return new UserDto("tinhtute", "trongtinh@lig.vn", "https://avatars.githubusercontent.com/u/1000000?v=4");
    }

    public static UserDto of(String username, String name, String email) {
        return new UserDto(username, name, email);
    }

    public static UserDto of(User user) {
        return new UserDto(user.getName(), user.getEmail(), user.getImageUrl());
    }
}
