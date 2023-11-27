package com.tsystems.dco.user;

import com.tsystems.dco.user.model.User;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

/**
 * Auditor aware implementation that uses {@link UserService} to get details for auditing.
 */
@Component
@RequiredArgsConstructor
public class UserAuditorAware implements AuditorAware<String> {

  private static final String CLAIM_AUDITOR = "sub";

  private final UserService userService;

  @Override
  public Optional<String> getCurrentAuditor() {
    return userService.getCurrentUser()
      .map(User::getClaims)
      .map(claims -> (String) claims.get(CLAIM_AUDITOR));
  }
}
