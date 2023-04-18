package com.example.oauthstarter.application.dto;

import com.example.oauthstarter.domain.model.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UserCreateDto(
        String name,
        @Email(message = "Email should be valid")
        String email,
        @Size(min = 8, message = "Password should be at least 8 characters")
        String password
) {
    public static UserCreateDto of(String name, String email, String password) {
        return new UserCreateDto(name, email, password);
    }

    public User toEntity() {
        return User.builder()
                .withName(name)
                .withEmail(email)
                .withPassword(password)
                .build();
    }
}
