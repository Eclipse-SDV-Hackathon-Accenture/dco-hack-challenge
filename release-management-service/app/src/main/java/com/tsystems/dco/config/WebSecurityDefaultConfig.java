package com.tsystems.dco.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Default security configuration that disables security.
 */
@Configuration
@EnableWebSecurity
@Profile("!oauth2")
public class WebSecurityDefaultConfig {

  /**
   * Configures security to be disabled.
   *
   * @param http the http security config
   * @return the security filter chain
   * @throws Exception any exception
   */
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.cors().disable();
    http.csrf().disable();
    http.authorizeRequests()
      .anyRequest().permitAll();
    return http.build();
  }
}
