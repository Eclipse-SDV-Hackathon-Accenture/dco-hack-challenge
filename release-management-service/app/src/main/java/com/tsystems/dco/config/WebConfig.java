package com.tsystems.dco.config;

import com.tsystems.dco.AppProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuration for MVC and cors.
 */
@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

  private final AppProperties properties;

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**")
      .allowedMethods("HEAD", "GET", "PUT", "POST", "DELETE", "PATCH", "OPTIONS")
      .allowedOrigins(properties.getCors().getOrigins())
      .allowedHeaders(properties.getCors().getHeaders());
  }
}
