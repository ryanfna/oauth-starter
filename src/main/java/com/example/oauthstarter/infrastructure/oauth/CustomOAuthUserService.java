package com.example.oauthstarter.infrastructure.oauth;

import com.example.oauthstarter.domain.constant.AuthProvider;
import com.example.oauthstarter.domain.dao.UserDao;
import com.example.oauthstarter.domain.model.AuthUserDetails;
import com.example.oauthstarter.domain.model.User;
import com.example.oauthstarter.infrastructure.exception.AppException;
import com.example.oauthstarter.infrastructure.oauth.user.OAuth2UserInfo;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Service
@AllArgsConstructor
public class CustomOAuthUserService extends DefaultOAuth2UserService {
    private final UserDao userDao;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);

        try {
            return processOAuth2User(oAuth2UserRequest, oAuth2User);
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            // Throwing an instance of AuthenticationException will trigger the OAuth2AuthenticationFailureHandler
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest userRequest, OAuth2User oAuth2User) {
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfo.of(userRequest.getClientRegistration().getRegistrationId(), oAuth2User.getAttributes());
        if (!StringUtils.hasText(oAuth2UserInfo.getEmail())) {
            throw new AppException("Email not found from OAuth2 provider");
        }

        Optional<User> userOpt = userDao.findByEmail(oAuth2UserInfo.getEmail());

        User user = userOpt.map(value -> {
                    if (!value.getProvider().equals(getProvider(userRequest))) {
                        throw new AppException("Looks like you're signed up with " +
                                value.getProvider() + " account. Please use your " +
                                value.getProvider() + " account to login.");
                    }
                    return updateExistingUser(value, oAuth2UserInfo);
                })
                .orElseGet(() -> registerNewUser(userRequest, oAuth2UserInfo));
        return AuthUserDetails.of(user);
    }

    private User registerNewUser(OAuth2UserRequest userRequest, OAuth2UserInfo oAuth2UserInfo) {
        User user = User.builder()
                .withEmail(oAuth2UserInfo.getEmail())
                .withName(oAuth2UserInfo.getName())
                .withEmailVerified(true)
                .withProvider(getProvider(userRequest))
                .withProviderId(oAuth2UserInfo.getId())
                .withImageUrl(oAuth2UserInfo.getImageUrl())
                .build();
        return userDao.save(user);
    }

    private User updateExistingUser(User user, OAuth2UserInfo oAuth2UserInfo) {
        user.setName(oAuth2UserInfo.getName());
        user.setImageUrl(oAuth2UserInfo.getImageUrl());
        return userDao.save(user);
    }

    private AuthProvider getProvider(OAuth2UserRequest userRequest) {
        return AuthProvider.valueOf(userRequest.getClientRegistration().getRegistrationId().toUpperCase());
    }
}
