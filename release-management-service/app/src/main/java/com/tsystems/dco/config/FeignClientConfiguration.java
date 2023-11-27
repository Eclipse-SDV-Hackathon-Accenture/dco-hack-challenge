package com.tsystems.dco.config;

import feign.auth.BasicAuthRequestInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Value;
@Configuration
public class FeignClientConfiguration {

  @Value("${track-management.username}")
  private String username;

  @Value("${track-management.password}")
  private String password;
  @Bean
  public BasicAuthRequestInterceptor basicAuthRequestInterceptor() {
    return new BasicAuthRequestInterceptor(username, password);
  }
}
