package com.example.oauthstarter.application.controller;

import com.example.oauthstarter.application.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@AllArgsConstructor
@RequiredArgsConstructor
public class DemoController {
    @Value("${application.oauth2.authorizedRedirectUris}")
    private String[] demoValue;

    @GetMapping
    public ResponseEntity<String> demo() {
        return ResponseEntity.ok("Demo - OAuth2 with Google!");
    }

    @GetMapping("/public")
    public ResponseEntity<UserDto> demoSecured() {
        return ResponseEntity.ok(UserDto.ofDefault());
    }

    @GetMapping("/demo")
    public ResponseEntity<Object> demoSecured2() {
        return ResponseEntity.ok(demoValue);
    }
}