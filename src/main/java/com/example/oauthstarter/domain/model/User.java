package com.example.oauthstarter.domain.model;

import com.example.oauthstarter.domain.constant.AuthProvider;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder(setterPrefix = "with")
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    private String password;
    private String imageUrl;
    @Builder.Default
    private Boolean emailVerified = false;
    @Builder.Default
    private AuthProvider provider = AuthProvider.ofDefault();
    private String providerId;
}
