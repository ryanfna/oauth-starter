package com.example.oauthstarter.domain.service;

import com.example.oauthstarter.application.dto.UserDto;
import com.example.oauthstarter.application.dto.auth.UserCreateDto;
import com.example.oauthstarter.domain.constant.ResponseCode;
import com.example.oauthstarter.domain.model.User;
import com.example.oauthstarter.domain.dao.UserDao;
import com.example.oauthstarter.infrastructure.exception.GlobalAppException;
import com.example.oauthstarter.infrastructure.exception.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class UserService {
    private final UserDao userDao;

    @Transactional
    public Long create(UserCreateDto dto) {
        if (existsByEmail(dto.email())) {
            throw new GlobalAppException(ResponseCode.CONFLICT, "User already exists");
        }
        return userDao.save(dto.toEntity())
                .getId();
    }

    public UserDto findByEmail(String email) {
        User user = userDao.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(ResponseCode.NOT_FOUND, "User not found"));
        return UserDto.of(user);
    }

    public UserDto updateAvatar(Long id, String avatarUrl) {
        User user = userDao.findById(id)
                .orElseThrow(() -> new NotFoundException(ResponseCode.NOT_FOUND, "User not found"));
        user.setImageUrl(avatarUrl);
        userDao.save(user);
        return UserDto.of(user);
    }

    private boolean existsByEmail(String email) {
        return userDao.existsUserByEmail(email);
    }
}
