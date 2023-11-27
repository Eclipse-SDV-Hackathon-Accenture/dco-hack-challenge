package com.tsystems.dco.user;

import com.tsystems.dco.user.model.User;
import feign.RequestInterceptor;
import java.util.Optional;

/**
 * Interface for a user service that returns {@link User} if available.
 */
public interface UserService {

  User getCurrentUserOrThrow();

  Optional<User> getCurrentUser();
  
  RequestInterceptor authenticationInterceptor();
}
