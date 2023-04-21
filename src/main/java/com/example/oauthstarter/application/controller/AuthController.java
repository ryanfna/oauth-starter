package com.example.oauthstarter.application.controller;

import com.example.oauthstarter.application.dto.auth.LoginInfoDto;
import com.example.oauthstarter.application.dto.auth.RefreshTokenDto;
import com.example.oauthstarter.application.dto.auth.UserCreateDto;
import com.example.oauthstarter.application.dto.auth.UserLoginDto;
import com.example.oauthstarter.application.dto.common.ResponseDto;
import com.example.oauthstarter.domain.service.UserService;
import com.example.oauthstarter.infrastructure.security.AuthService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Long> register(@RequestBody @Valid UserCreateDto dto) {
        return ResponseEntity.ok(userService.create(dto));
    }

    @PostMapping("/login")
    public ResponseDto<LoginInfoDto> login(@RequestBody @Valid UserLoginDto dto) {
        return ResponseDto.ok(authService.login(dto));
    }

    @PostMapping("/refresh")
    public ResponseDto<LoginInfoDto> refresh(@RequestBody RefreshTokenDto dto) {
        return ResponseDto.ok(authService.refresh(dto));
    }
}
