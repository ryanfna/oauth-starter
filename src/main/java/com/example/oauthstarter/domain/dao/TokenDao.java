package com.example.oauthstarter.domain.dao;

import com.example.oauthstarter.domain.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenDao extends JpaRepository<Token, Long> {
    void deleteTokensByUserId(Long userId);
}
