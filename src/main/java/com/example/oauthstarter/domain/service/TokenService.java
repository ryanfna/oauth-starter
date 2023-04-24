package com.example.oauthstarter.domain.service;

import com.example.oauthstarter.domain.dao.TokenDao;
import com.example.oauthstarter.domain.model.Token;
import com.example.oauthstarter.domain.model.User;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TokenService {
    private final TokenDao tokenDao;

    public void storeToken(String value, Long userId) {
        Token token = new Token();
        token.setToken(value);
        token.setUser(User.builder().withId(userId).build());
        tokenDao.save(token);
    }

    @Transactional
    public void deleteToken(Long userId) {
        tokenDao.deleteTokensByUserId(userId);
    }
}
