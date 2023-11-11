package com.safer.safer.auth.presentation;

import com.safer.safer.auth.dto.AccessTokenResponse;
import com.safer.safer.auth.dto.UserTokens;
import com.safer.safer.auth.application.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

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
            @RequestParam("code") final String authorizationCode
    ) {
        UserTokens tokens = authService.login(provider, authorizationCode);
        //ResponseCookie cookie = authService.generateCookie(tokens.refreshToken());
        //response.addHeader(SET_COOKIE, cookie.toString());

        return ResponseEntity.ok(AccessTokenResponse.of(tokens.accessToken()));
    }
}
