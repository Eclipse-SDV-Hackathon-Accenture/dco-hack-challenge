package com.tsystems.dco.tm.integration.tminterface.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Email Service Properties
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Configuration
@ConfigurationProperties(prefix = "spring.main")
public class EmailServiceProperties {

  private String host;
  private String port;
  private String username;
  private String password;
}

