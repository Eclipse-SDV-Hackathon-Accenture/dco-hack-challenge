package com.tsystems.dco.tm.integration.tminterface.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * task management properties
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Configuration
@ConfigurationProperties(prefix = "task-management")
public class TaskManagementProperties {

  private String host;
  private String port;
  private String name;
  private String namespace;
}

