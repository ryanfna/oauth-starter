package com.example.oauthstarter.infrastructure.oauth;

import com.example.oauthstarter.domain.constant.AuthProvider;
import com.example.oauthstarter.domain.dao.UserDao;
import com.example.oauthstarter.domain.model.AuthUserDetails;
import com.example.oauthstarter.domain.model.User;
import com.example.oauthstarter.infrastructure.exception.GlobalAppException;
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
        if (StringUtils.hasText(oAuth2UserInfo.getEmail())) {
            throw new GlobalAppException("Email not found from OAuth2 provider");
        }

        User user = userDao.findByEmail(oAuth2UserInfo.getEmail())
                .orElse(registerNewUser(userRequest, oAuth2UserInfo));
        if (!user.getProvider().equals(AuthProvider.valueOf(userRequest.getClientRegistration().getRegistrationId()))) {
            throw new GlobalAppException("Looks like you're signed up with " +
                    user.getProvider() + " account. Please use your " +
                    user.getProvider() + " account to login.");
        }
        user = updateExistingUser(user, oAuth2UserInfo);
        return AuthUserDetails.of(user);
    }

    private User registerNewUser(OAuth2UserRequest userRequest, OAuth2UserInfo oAuth2UserInfo) {
        return User.builder()
                .withEmail(oAuth2UserInfo.getEmail())
                .withName(oAuth2UserInfo.getName())
                .withEmailVerified(true)
                .withProvider(AuthProvider.valueOf(userRequest.getClientRegistration().getRegistrationId()))
                .withProviderId(oAuth2UserInfo.getId())
                .withImageUrl(oAuth2UserInfo.getImageUrl())
                .build();
    }

    private User updateExistingUser(User user, OAuth2UserInfo oAuth2UserInfo) {
        user.setName(oAuth2UserInfo.getName());
        user.setImageUrl(oAuth2UserInfo.getImageUrl());
        return userDao.save(user);
    }
}
