package com.example.oauthstarter.domain.constant;

//TODO: rèfactor this
public enum AuthProvider {
    local,
    facebook,
    google,
    github;


    public static AuthProvider ofDefault() {
        return local;
    }
}
