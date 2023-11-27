package com.tsystems.dco.user;

import com.tsystems.dco.base.BaseException;
import com.tsystems.dco.user.model.User;

import feign.RequestInterceptor;

import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

/**
 * Implementation of {@link UserService} that extracts {@link User}
 * from {@link JwtAuthenticationToken} or {@link OAuth2AuthenticationToken}.
 */
@Slf4j
@Service
@Profile("oauth2")
@RequiredArgsConstructor
public class UserServiceOAuth2 implements UserService {

  private final OAuth2AuthorizedClientService authorizedClientService;

  @Override
  public User getCurrentUserOrThrow() {
    return getCurrentUser().orElseThrow(() -> new BaseException(HttpStatus.UNAUTHORIZED, "User not present in request."));
  }

  @Override
  public Optional<User> getCurrentUser() {
    var authentication = Optional.ofNullable(SecurityContextHolder.getContext())
      .map(SecurityContext::getAuthentication);
    Optional<User> user = Optional.empty();
    if (authentication.isPresent() && authentication.get() instanceof JwtAuthenticationToken token) {
      user = Optional.of(User.builder()
          .claims(token.getTokenAttributes())
          .token(token.getToken().getTokenValue())
          .tokenType("Bearer")
          .authorities(authentication.get().getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toSet()))
        .build());
    }
    if (authentication.isPresent() && authentication.get() instanceof OAuth2AuthenticationToken token) {
      var client = authorizedClientService
        .loadAuthorizedClient(token.getAuthorizedClientRegistrationId(), token.getName());
      user = Optional.of(User.builder()
        .claims(token.getPrincipal().getAttributes())
        .token(client.getAccessToken().getTokenValue())
        .tokenType(client.getAccessToken().getTokenType().getValue())
        .authorities(authentication.get().getAuthorities().stream()
          .map(GrantedAuthority::getAuthority)
          .collect(Collectors.toSet()))
        .build());
    }
    return user;
  }
  
  @Override
  public RequestInterceptor authenticationInterceptor() {
    return template -> {
      var user = getCurrentUser();
      if (user.isPresent()) {
        log.debug("getRequestInterceptor() detected authentication and forwarding token.");
        template.header(HttpHeaders.AUTHORIZATION);
        template.header(HttpHeaders.AUTHORIZATION, String.format("%s %s",
          user.get().getTokenType(),
          user.get().getToken())
        );
      } else {
        log.warn("getRequestInterceptor() no authentication detected and failed to forwarding token.");
      }
    };
  }
}
