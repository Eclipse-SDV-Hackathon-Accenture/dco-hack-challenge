package com.tsystems.dco.release.integration.track.client;

import com.tsystems.dco.user.UserService;
import feign.RequestInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

@RequiredArgsConstructor
public class TrackFeignClientConfig {

  @Bean
  @Profile("oauth2")
  public RequestInterceptor authenticationInterceptor(UserService userService) {
    return userService.authenticationInterceptor();
  }
}
