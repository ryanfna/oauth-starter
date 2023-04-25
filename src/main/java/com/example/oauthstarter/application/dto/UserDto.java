package com.example.oauthstarter.application.dto;

import com.example.oauthstarter.domain.constant.AuthProvider;
import com.example.oauthstarter.domain.constant.UserRole;
import com.example.oauthstarter.domain.model.User;

public record UserDto(String name,
                      String email,
                      String imageUrl,
                      AuthProvider provider,
                      UserRole role) {
    public static UserDto ofDefault() {
        return new UserDto("tinhtute",
                "trongtinh@lig.vn",
                "https://avatars.githubusercontent.com/u/1000000?v=4",
                AuthProvider.ofDefault(),
                UserRole.ofDefault());
    }

    public static UserDto of(String username, String name, String email, String provider, String role) {
        return new UserDto(username, name, email, AuthProvider.valueOf(provider), UserRole.valueOf(role));
    }

    public static UserDto of(User user) {
        return new UserDto(user.getName(), user.getEmail(), user.getImageUrl(), user.getProvider(), user.getRole());
    }
}
