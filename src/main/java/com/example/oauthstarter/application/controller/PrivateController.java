package com.example.oauthstarter.application.controller;

import com.example.oauthstarter.application.dto.UserDto;
import com.example.oauthstarter.application.dto.common.ResponseDto;
import com.example.oauthstarter.domain.model.AuthUserDetails;
import com.example.oauthstarter.domain.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/private")
@AllArgsConstructor
public class PrivateController {
    private final UserService userService;

    @GetMapping("/demo")
    public ResponseDto<UserDto> privateUser() {
        return ResponseDto.ok(UserDto.ofDefault());
    }

    @GetMapping("/user")
    public ResponseDto<UserDto> user(@AuthenticationPrincipal OAuth2User principal) {
        AuthUserDetails authUser = (AuthUserDetails) principal;
        String email = authUser.getUsername();
        return ResponseDto.ok(userService.findByEmail(email));
    }
}
