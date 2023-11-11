package com.safer.safer.service;

import com.safer.safer.auth.*;
import com.safer.safer.domain.User;
import com.safer.safer.dto.UserTokens;
import com.safer.safer.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private static final int COOKIE_MAX_AGE = 60 * 60 * 24 * 7;

    private final UserRepository userRepository;
    private final OAuthProviderComposite oAuthProviderComposite;
    private final JwtProvider jwtProvider;

    public UserTokens login(String providerType, String code) {
        OauthProvider provider = oAuthProviderComposite.matchProvider(providerType);
        OAuthUserInfo userInfo = provider.getUserInfo(code);
        User user = findOrRegister(
                userInfo.getEmail(),
                userInfo.getName(),
                ProviderType.from(providerType)
        );
        UserTokens tokens = jwtProvider.createTokens(user.getId().toString());
        //TODO: refreshToken Redis 저장
        return tokens;
    }

    private User findOrRegister(String email, String name, ProviderType providerType) {
        return userRepository.findByEmailAndProviderType(email, providerType)
                .orElseGet(() -> registerUser(email, name, providerType));
    }

    private User registerUser(String email, String name, ProviderType providerType) {
        return userRepository.save(User.of(email, name, providerType));
    }

    public String generateLoginRedirectUri(String providerType) {
        OauthProvider provider = oAuthProviderComposite.matchProvider(providerType);
        return provider.getLoginRedirectUri();
    }

    public ResponseCookie generateCookie(String refreshToken) {
        return ResponseCookie.from("refresh-token", refreshToken)
                .maxAge(COOKIE_MAX_AGE)
                .sameSite("None")
                .secure(true)
                .httpOnly(true)
                .path("/")
                .build();
    }
}
