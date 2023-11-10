package com.safer.safer.controller;

import com.safer.safer.dto.AccessTokenResponse;
import com.safer.safer.dto.TokensResponse;
import com.safer.safer.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

import static org.springframework.http.HttpHeaders.SET_COOKIE;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/oauth")
public class AuthController {

    private final AuthService authService;

    @GetMapping("/{provider}")
    public ResponseEntity<Void> findLoginRedirectUri(@PathVariable final String provider) {
        String loginRedirectUri = authService.generateLoginRedirectUri(provider);
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(loginRedirectUri))
                .build();
    }

    @GetMapping("/login/{provider}")
    public ResponseEntity<AccessTokenResponse> login(
            @PathVariable final String provider,
            @RequestParam("code") final String authorizationCode,
            HttpServletResponse response
    ) {
        TokensResponse tokens = authService.login(provider, authorizationCode);
        ResponseCookie cookie = authService.generateCookie(tokens.refreshToken());
        response.addHeader(SET_COOKIE, cookie.toString());

        return ResponseEntity.ok(AccessTokenResponse.of(tokens.accessToken()));
    }
}
