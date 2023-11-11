package com.safer.safer.auth;

import com.safer.safer.dto.UserTokens;
import com.safer.safer.exception.JwtException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import static com.safer.safer.exception.ExceptionCode.EXPIRED_ACCESS_TOKEN;
import static com.safer.safer.exception.ExceptionCode.INVALID_ACCESS_TOKEN;

@Component
public class JwtProvider {

    private final SecretKey secretKey;
    private final Long accessTokenExpirationTime;
    private final Long refreshTokenExpirationTime;

    public JwtProvider(
            @Value("${jwt.secret-key}") String secretKey,
            @Value("${jwt.access-token-expiration-time}") Long accessTokenExpirationTime,
            @Value("${jwt.refresh-token-expiration-time}") Long refreshTokenExpirationTime
    ) {
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        this.accessTokenExpirationTime = accessTokenExpirationTime;
        this.refreshTokenExpirationTime = refreshTokenExpirationTime;
    }

    public UserTokens createTokens(final String subject) {
        String accessToken = createToken(subject, accessTokenExpirationTime);
        String refreshToken = createToken("", refreshTokenExpirationTime);
        return UserTokens.of(accessToken, refreshToken);
    }

    private String createToken(final String subject, Long expirationTime) {
        Date now = new Date();
        Date until = new Date(now.getTime() + expirationTime);

        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(until)
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();
    }
}
