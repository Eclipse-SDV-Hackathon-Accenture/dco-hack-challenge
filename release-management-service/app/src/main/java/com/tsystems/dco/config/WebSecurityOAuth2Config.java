package com.tsystems.dco.config;

import com.tsystems.dco.oauth2.OAuth2AuthoritiesConverter;
import com.tsystems.dco.oauth2.OAuth2Properties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;

/**
 * OAuth2 security configuration that enabled oauth2 security.
 */
@Slf4j
@Profile("oauth2")
@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityOAuth2Config {

  private final OAuth2Properties properties;
  private final OAuth2AuthoritiesConverter authoritiesConverter;
  private final ClientRegistrationRepository clientRegistrationRepository;

  /**
   * Configures security to use oauth2.
   *
   * @param http the http security config
   * @return the security filter chain
   * @throws Exception any exception
   */
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.cors().disable();
    http.csrf().disable();
    http.httpBasic().disable();
    http.formLogin().disable();
    http.sessionManagement()
      .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    http.authorizeRequests()
      .mvcMatchers("/error").permitAll()
      .mvcMatchers("/management/**").permitAll()
      .anyRequest().authenticated();
    http.oauth2ResourceServer()
      .jwt()
      .jwtAuthenticationConverter(authoritiesConverter.jwt());
    if (properties.getLogin().isEnabled()) {
      http.sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED);
      http.oauth2Login()
        .permitAll()
        .userInfoEndpoint()
        .userAuthoritiesMapper(authoritiesConverter.oauth2());
      var logoutSuccessHandler = new OidcClientInitiatedLogoutSuccessHandler(clientRegistrationRepository);
      logoutSuccessHandler.setPostLogoutRedirectUri("{baseUrl}");
      http.logout()
        .permitAll()
        .logoutSuccessHandler(logoutSuccessHandler);
    }
    return http.build();
  }
}
