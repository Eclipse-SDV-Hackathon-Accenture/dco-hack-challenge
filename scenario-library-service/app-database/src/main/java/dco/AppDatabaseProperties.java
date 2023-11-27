package dco;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Configuration;

/**
 * Properties of application for database migrations.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Configuration
@ConfigurationProperties(prefix = "app")
public class AppDatabaseProperties {

  /**
   * The app name.
   */
  private String name;
  /**
   * The app version.
   */
  private String version;
  /**
   * The postgres properties.
   */
  @NestedConfigurationProperty
  private Postgres postgres;

  /**
   * Properties of postgres.
   */
  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Postgres {

    /**
     * The postgres host.
     */
    private String host;
    /**
     * The postgres port.
     */
    private Integer port;
    /**
     * The postgres database.
     */
    private String database;
    /**
     * The postgres username.
     */
    private String username;
    /**
     * The postgres password.
     */
    private String password;
  }
}
