package com.tsystems.dco.user;

import com.tsystems.dco.base.BaseException;
import com.tsystems.dco.user.model.User;

import feign.RequestInterceptor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

/**
 * Implementation of {@link UserService} that returns a default {@link User}.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Profile("!oauth2")
public class UserServiceDefault implements UserService {

  @Override
  public User getCurrentUserOrThrow() {
    return getCurrentUser().orElseThrow(() -> new BaseException(HttpStatus.UNAUTHORIZED, "User not available."));
  }

  @Override
  public Optional<User> getCurrentUser() {
    var claims = new HashMap<String, Object>();
    claims.put("sub", "unknown");
    claims.put("email", "unknown");
    return Optional.of(User.builder()
      .claims(claims)
      .authorities(new HashSet<>())
      .build());
  }
  
  @Override
  public RequestInterceptor authenticationInterceptor() {
    return template -> {};
  }
}
