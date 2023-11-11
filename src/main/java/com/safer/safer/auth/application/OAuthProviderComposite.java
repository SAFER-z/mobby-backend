package com.safer.safer.auth.application;

import com.safer.safer.auth.domain.ProviderType;
import com.safer.safer.auth.exception.OAuthException;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static com.safer.safer.common.exception.ExceptionCode.UNSUPPORTED_OAUTH_TYPE;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

@Component
public class OAuthProviderComposite {

    private final Map<ProviderType, OauthProvider> providers;

    public OAuthProviderComposite(final Set<OauthProvider> providers) {
        this.providers = providers.stream()
                .collect(toMap(OauthProvider::getType, identity()));
    }

    public OauthProvider matchProvider(final String providerType) {
        return Optional.ofNullable(providers.get(ProviderType.from(providerType)))
                .orElseThrow(() -> new OAuthException(UNSUPPORTED_OAUTH_TYPE));
    }
}
