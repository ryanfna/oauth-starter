package com.example.oauthstarter.application.dto;

import com.example.oauthstarter.domain.constant.AuthProvider;
import com.example.oauthstarter.domain.model.User;

public record UserDto(String name, String email, String imageUrl, AuthProvider provider) {
    public static UserDto ofDefault() {
        return new UserDto("tinhtute",
                "trongtinh@lig.vn",
                "https://avatars.githubusercontent.com/u/1000000?v=4",
                AuthProvider.ofDefault());
    }

    public static UserDto of(String username, String name, String email, String provider) {
        return new UserDto(username, name, email, AuthProvider.valueOf(provider));
    }

    public static UserDto of(User user) {
        return new UserDto(user.getName(), user.getEmail(), user.getImageUrl(), user.getProvider());
    }
}
