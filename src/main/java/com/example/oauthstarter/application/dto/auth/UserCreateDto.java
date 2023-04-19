package com.example.oauthstarter.application.dto.auth;

import com.example.oauthstarter.domain.model.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

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
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return User.builder()
                .withName(name)
                .withEmail(email)
                .withPassword(passwordEncoder.encode(password))
                .build();
    }
}
