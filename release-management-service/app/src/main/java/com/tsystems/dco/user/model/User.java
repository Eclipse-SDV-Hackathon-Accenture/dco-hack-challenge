package com.tsystems.dco.user.model;

import java.util.Map;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * User data.
 */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class User {

  private String token;
  private String tokenType;
  private Set<String> authorities;
  private Map<String, Object> claims;
}
