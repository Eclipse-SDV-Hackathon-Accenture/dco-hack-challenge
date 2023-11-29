package com.tsystems.dco;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * Entrypoint of application for database migrations.
 */
@Slf4j
@EnableConfigurationProperties
@SpringBootApplication
public class AppDatabase {

  /**
   * Main application entrypoint.
   *
   * @param args command line arguments
   */
  public static void main(String[] args) {
    var ctx = SpringApplication.run(AppDatabase.class, args);
    var code = SpringApplication.exit(ctx);
    System.exit(code);
  }
}
