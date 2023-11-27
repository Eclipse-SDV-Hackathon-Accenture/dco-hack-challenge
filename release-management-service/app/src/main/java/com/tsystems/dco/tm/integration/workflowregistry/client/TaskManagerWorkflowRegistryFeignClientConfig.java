package com.tsystems.dco.tm.integration.workflowregistry.client;

import com.tsystems.dco.user.UserService;
import feign.RequestInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

@RequiredArgsConstructor
public class TaskManagerWorkflowRegistryFeignClientConfig {

  @Bean
  @Profile("oauth2")
  public RequestInterceptor authenticationInterceptor(UserService userService) {
    return userService.authenticationInterceptor();
  }
}
