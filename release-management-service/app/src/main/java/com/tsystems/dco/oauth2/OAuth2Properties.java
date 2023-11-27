package com.tsystems.dco.oauth2;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Configuration;

/**
 * Properties of oauth2.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Configuration
@ConfigurationProperties(prefix = "app.oauth2")
public class OAuth2Properties {

  /**
   * The oauth2 issuer url.
   */
  private String issuer;
  /**
   * The oauth2 client properties.
   */
  @NestedConfigurationProperty
  private Client client;
  /**
   * The oauth2 login properties.
   */
  @NestedConfigurationProperty
  private Login login;
  /**
   * The oauth2 authorities properties.
   */
  @NestedConfigurationProperty
  private Authorities authorities;

  /**
   * Properties of OAuth2 login.
   */
  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Login {

    /**
     * Enable oauth2 login feature.
     */
    private boolean enabled;
  }

  /**
   * Properties of OAuth2 client.
   */
  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Client {

    /**
     * The oauth2 client id.
     */
    private String id;
    /**
     * The oauth2 client secret.
     */
    private String secret;
  }

  /**
   * Properties of OAuth2 authorities to map.
   */
  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Authorities {

    /**
     * The oauth2 authorities prefix to attach.
     */
    private String prefix;
    /**
     * The oauth2 authorities claims to analyse from token.
     */
    @NestedConfigurationProperty
    private List<String> claims;
    /**
     * The oauth2 authorities mappings.
     */
    @NestedConfigurationProperty
    private List<Mapping> mappings;

    /**
     * Properties of OAuth2 authorities mapping.
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Mapping {

      /**
       * The oauth2 authorities mapping patterns to match.
       */
      private List<String> patterns;
      /**
       * The oauth2 authorities to assign on matching pattern.
       */
      private List<String> authorities;
    }
  }
}
