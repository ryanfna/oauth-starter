package com.example.oauthstarter.application.dto.auth;

import com.example.oauthstarter.application.validator.ValidEnum;
import com.example.oauthstarter.domain.constant.UserRole;
import com.example.oauthstarter.domain.model.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public record UserCreateDto(
        @NotEmpty(message = "Name should not be empty")
        String name,
        @NotEmpty(message = "Email should not be empty")
        @Email(message = "Email should be valid")
        String email,
        @Size(min = 8, message = "Password should be at least 8 characters")
        String password,
        @ValidEnum(enumClass = UserRole.class, message = "Role should be one of USER, ADMIN, MODERATOR")
        String role
) {
    public static UserCreateDto of(String name, String email, String password, String role) {
        return new UserCreateDto(name, email, password, role);
    }

    public User toEntity() {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return User.builder()
                .withName(name)
                .withEmail(email)
                .withPassword(passwordEncoder.encode(password))
                .withRole(UserRole.valueOf(role))
                .build();
    }
}
