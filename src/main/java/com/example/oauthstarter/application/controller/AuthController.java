package com.example.oauthstarter.application.controller;

import com.example.oauthstarter.application.dto.auth.LoginInfoDto;
import com.example.oauthstarter.application.dto.auth.UserCreateDto;
import com.example.oauthstarter.application.dto.auth.UserLoginDto;
import com.example.oauthstarter.application.dto.common.ResponseDto;
import com.example.oauthstarter.domain.service.UserService;
import com.example.oauthstarter.infrastructure.utils.TokenProvider;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;

    @PostMapping("/register")
    public ResponseEntity<Long> register(@RequestBody @Valid UserCreateDto dto) {
        return ResponseEntity.ok(userService.create(dto));
    }

    @PostMapping("/login")
    public ResponseDto<LoginInfoDto> login(@RequestBody @Valid UserLoginDto dto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.email(), dto.password())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return ResponseDto.ok(LoginInfoDto.of(tokenProvider.createToken(authentication)));
    }
}
