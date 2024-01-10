package com.safer.safer.auth.application;

import com.safer.safer.auth.dto.UserTokens;
import com.safer.safer.auth.exception.JwtException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

import static com.safer.safer.common.exception.ExceptionCode.EXPIRED_ACCESS_TOKEN;
import static com.safer.safer.common.exception.ExceptionCode.INVALID_ACCESS_TOKEN;

@RequiredArgsConstructor
@Component
public class JwtProvider {

    @Value("${jwt.secret}")
    private String secretKey;
    private Key key;
    private final static long accessTokenValidTime = (60 * 1000) * 30;
    private final static long refreshTokenValidTime = (60 * 1000) * 60 * 24 * 7;

    @PostConstruct
    protected void init() {
        String encodedKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
        key= Keys.hmacShaKeyFor(encodedKey.getBytes());
    }

    public UserTokens createTokens(final String subject) {
        String accessToken = createToken(subject, accessTokenValidTime);
        String refreshToken = createToken("", refreshTokenValidTime);
        return UserTokens.of(accessToken, refreshToken);
    }

    private String createToken(final String subject, Long expirationTime) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + expirationTime);

        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public void validateToken(String token) {
        try {
            parseToken(token);
        } catch (ExpiredJwtException e) {
            throw new JwtException(EXPIRED_ACCESS_TOKEN);
        } catch (io.jsonwebtoken.JwtException | IllegalArgumentException e) {
            throw new JwtException(INVALID_ACCESS_TOKEN);
        }
    }

    public String getSubject(String token) {
        return parseToken(token)
                .getBody()
                .getSubject();
    }

    private Jws<Claims> parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
    }
}
