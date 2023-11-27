package com.tsystems.dco.oauth2;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.NestedNullException;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.context.annotation.Profile;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

/**
 * Authorities converter that converts values of JWT claims to authorities.
 */
@Slf4j
@Component
@Profile("oauth2")
@RequiredArgsConstructor
public class OAuth2AuthoritiesConverter {

  private final OAuth2Properties properties;

  /**
   * Converter that converts {@link Jwt} claims to {@link SimpleGrantedAuthority}.
   *
   * @return the converter
   */
  public Converter<Jwt, AbstractAuthenticationToken> jwt() {
    return jwt -> new JwtAuthenticationToken(jwt, getAuthoritiesFromClaimsByProperties(jwt.getClaims()));
  }

  /**
   * Mapper that maps {@link OAuth2UserAuthority} to {@link SimpleGrantedAuthority}.
   *
   * @return the mapper
   */
  public GrantedAuthoritiesMapper oauth2() {
    return authorities -> {
      var mapped = new HashSet<GrantedAuthority>();
      for (var authority : authorities) {
        if (authority instanceof OAuth2UserAuthority) {
          var claims = Optional.of((OAuth2UserAuthority) authority)
            .map(OAuth2UserAuthority::getAttributes)
            .orElse(new HashMap<>());
          mapped.addAll(getAuthoritiesFromClaimsByProperties(claims));
        }
      }
      return mapped;
    };
  }

  /**
   * Get a set of {@link SimpleGrantedAuthority} mapped by application configurations.
   *
   * @param claims the claims of token
   * @return the set {@link SimpleGrantedAuthority}
   */
  public Set<SimpleGrantedAuthority> getAuthoritiesFromClaimsByProperties(
    Map<String, Object> claims
  ) {
    var prefix = properties.getAuthorities().getPrefix();
    var values = getClaimValuesByFields(claims, properties.getAuthorities().getClaims());
    var mappings = properties.getAuthorities().getMappings();
    return values.stream().flatMap(value -> mappings.stream()
        .filter(mapping -> mapping.getPatterns().stream()
          .map(Pattern::compile)
          .anyMatch(pattern -> pattern.matcher(value).matches()))
        .flatMap(mapping -> mapping.getAuthorities().stream()))
      .map(authority -> new SimpleGrantedAuthority(prefix + authority))
      .collect(Collectors.toSet());
  }

  /**
   * Get a set of claim values by a list of claim fields.
   *
   * @param claims the claims where to extract the values
   * @param fields the claim fields to extract
   * @return the extracted claim values
   */
  public Set<String> getClaimValuesByFields(Map<String, Object> claims, List<String> fields) {
    return fields.stream()
      .flatMap(field -> getClaimValuesByField(claims, field).stream())
      .collect(Collectors.toSet());
  }

  /**
   * Get a set of claim values by a claim field.
   *
   * @param claims the claims where to extract the values
   * @param field the claim field to extract
   * @return the extracted claim values
   */
  public Set<String> getClaimValuesByField(Map<String, Object> claims, String field) {
    try {
      var raw = PropertyUtils.getProperty(claims, field);
      if (raw instanceof List<?> list) {
        return list.stream()
          .filter(String.class::isInstance)
          .map(String.class::cast)
          .collect(Collectors.toSet());
      }
      if (raw instanceof String) {
        return Collections.singleton((String) raw);
      }
    } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | NestedNullException e) {
      log.debug("Could not parse field {} from claim set {}. {}", field, claims.keySet(), e.getMessage());
    }
    return Collections.emptySet();
  }
}
