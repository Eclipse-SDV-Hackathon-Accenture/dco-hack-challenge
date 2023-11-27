package com.tsystems.dco;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Entrypoint of application.
 */
@EnableAsync
@EnableCaching
@EnableConfigurationProperties
@EnableJpaAuditing
@EnableJpaRepositories
@EnableFeignClients
@SpringBootApplication
public class App {

  private static final Logger logger = LoggerFactory.getLogger(App.class);

  /**
   * Main application entrypoint.
   *
   * @param args command line arguments
   */
  public static void main(String[] args) {
    SpringApplication.run(App.class, args);
    logger.info("******Release Management Started******");
  }
}
