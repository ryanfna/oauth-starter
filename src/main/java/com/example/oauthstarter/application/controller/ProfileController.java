package com.example.oauthstarter.application.controller;

import com.example.oauthstarter.application.dto.UserDto;
import com.example.oauthstarter.application.dto.common.ResponseDto;
import com.example.oauthstarter.application.dto.profile.UpdateAvatarDto;
import com.example.oauthstarter.domain.model.AuthUserDetails;
import com.example.oauthstarter.domain.service.UserService;
import com.example.oauthstarter.infrastructure.minio.MinioService;
import io.minio.errors.*;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/profile")
@AllArgsConstructor
public class ProfileController {
    private final MinioService minioService;
    private final UserService userService;

    @GetMapping("/get-presigned-url")
    public ResponseDto<String> getPresignedUrl(@RequestParam String filename) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        return ResponseDto.ok(minioService.generatePresignedUrl(filename));
    }

    @PostMapping("/upload-avatar")
    public ResponseDto<UserDto> uploadAvatar(@AuthenticationPrincipal OAuth2User principal,
                                             @RequestBody UpdateAvatarDto dto) {
        AuthUserDetails authUser = (AuthUserDetails) principal;
        var userId = authUser.getId();
        var userDto = userService.updateAvatar(userId, dto.avatarUrl());
        return ResponseDto.ok(userDto);
    }
}
