package com.example.oauthstarter.application;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/private")
public class PrivateController {

    @GetMapping("/private-user")
    public ResponseEntity<UserDto> privateUser() {
        return ResponseEntity.ok(UserDto.of(
                "private-user",
                "Private User",
                "private@lig.vn"));
    }

    @GetMapping("/user")
    public Map<String, Object> user(@AuthenticationPrincipal OAuth2User principal) {
        return Collections.singletonMap("name", principal);
    }
}
