package com.safer.safer.user.domain;

import com.safer.safer.auth.domain.ProviderType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "user_account")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String name;

    @Enumerated(value = EnumType.STRING)
    private ProviderType providerType;

    private User(String email, String name, ProviderType providerType) {
        this.email = email;
        this.name = name;
        this.providerType = providerType;
    }

    public static User of(String email, String name, ProviderType providerType) {
        return new User(email, name, providerType);
    }
}
