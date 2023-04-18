package com.example.oauthstarter.domain.service;

import com.example.oauthstarter.domain.model.AuthUserDetails;
import com.example.oauthstarter.domain.model.User;
import com.example.oauthstarter.domain.dao.UserDao;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthUserDetailsService implements UserDetailsService {
    private final UserDao userDao;

    @Override
    public AuthUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return AuthUserDetails.of(user);
    }
}
