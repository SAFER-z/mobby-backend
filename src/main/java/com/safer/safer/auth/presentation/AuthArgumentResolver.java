package com.safer.safer.auth.presentation;

import com.safer.safer.auth.application.JwtProvider;
import com.safer.safer.auth.dto.UserInfo;
import com.safer.safer.auth.exception.AuthException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Optional;

import static com.safer.safer.common.exception.ExceptionCode.INVALID_ACCESS_TOKEN;
import static com.safer.safer.common.exception.ExceptionCode.INVALID_REQUEST;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
@RequiredArgsConstructor
public class AuthArgumentResolver implements HandlerMethodArgumentResolver {

    private static final String BEARER_TYPE = "Bearer ";

    private final JwtProvider jwtProvider;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(UserInfo.class) &&
                parameter.hasParameterAnnotation(Auth.class);
    }

    @Override
    public UserInfo resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory
    ) {
        HttpServletRequest request = Optional.ofNullable(webRequest.getNativeRequest(HttpServletRequest.class))
                .orElseThrow(() -> new AuthException(INVALID_REQUEST));

        String accessToken = extractAccessToken(request.getHeader(AUTHORIZATION));
        jwtProvider.validateToken(accessToken);

        final Long userId = Long.valueOf(jwtProvider.getSubject(accessToken));
        return UserInfo.of(userId);
    }

    private String extractAccessToken(String header) {
        if(StringUtils.hasText(header) && header.startsWith(BEARER_TYPE)) {
            return header.substring(BEARER_TYPE.length()).trim();
        }
        throw new AuthException(INVALID_ACCESS_TOKEN);
    }
}
