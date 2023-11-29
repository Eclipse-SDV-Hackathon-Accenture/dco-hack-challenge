package com.tsystems.dco.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Value("${app.username}")
  private String username;
  @Value("${app.password}")
  private String password;

  @Bean
  public SecurityFilterChain configure(HttpSecurity http) throws Exception {
    return http.cors().and().csrf().disable()
      .authorizeRequests(auth -> auth.anyRequest().authenticated())
      .httpBasic(withDefaults())
      .build();
  }

  @Bean
  public InMemoryUserDetailsManager userDetailsManager() {

    UserDetails user1 = User.withDefaultPasswordEncoder()
      .username(username)
      .password(password)
      .roles("USER")
      .build();

    UserDetails user2 = User.withDefaultPasswordEncoder()
      .username("dco")
      .password("dco")
      .roles("USER")
      .build();

    UserDetails admin = User.withDefaultPasswordEncoder()
      .username("admin")
      .password("password")
      .roles("USER","ADMIN")
      .build();

    return new InMemoryUserDetailsManager(user1, user2, admin);
  }

}
