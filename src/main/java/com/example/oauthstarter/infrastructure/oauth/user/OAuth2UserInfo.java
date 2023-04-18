package com.example.oauthstarter.infrastructure.oauth.user;

import com.example.oauthstarter.infrastructure.exception.GlobalAppException;

import java.util.Map;

public abstract class OAuth2UserInfo {
    protected Map<String, Object> attributes;

    protected OAuth2UserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public static OAuth2UserInfo of(String registrationId, Map<String, Object> attributes) {
        if (registrationId.equalsIgnoreCase("google")) {
            return new GoogleUserInfo(attributes);
        } else {
            throw new GlobalAppException("Sorry! Login with " + registrationId + " is not supported yet.");
        }
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public abstract String getId();

    public abstract String getName();

    public abstract String getEmail();

    public abstract String getImageUrl();
}