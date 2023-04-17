package com.example.oauthstarter.application;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class DemoController {
    @GetMapping
    public ResponseEntity<String> demo() {
        return ResponseEntity.ok("Demo - OAuth2 with Google!");
    }

    @GetMapping("/public")
    public ResponseEntity<UserDto> demoSecured() {
        return ResponseEntity.ok(UserDto.ofDefault());
    }
}