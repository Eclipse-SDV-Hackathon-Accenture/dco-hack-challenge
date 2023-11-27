/*
 *   ========================================================================
 *  SDV Developer Console
 *
 *   Copyright (C) 2022 - 2023 T-Systems International GmbH
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 *   SPDX-License-Identifier: Apache-2.0
 *
 *   ========================================================================
 */

package com.tsystems.dco;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Configuration;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Properties of application.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Configuration
@ConfigurationProperties(prefix = "app")
public class AppProperties {

  /**
   * The app name.
   */
  private String name;
  /**
   * The app version.
   */
  private String version;
  /**
   * The rest properties.
   */
  @NestedConfigurationProperty
  private Rest rest;
  /**
   * The probes properties.
   */
  @NestedConfigurationProperty
  private Probes probes;
  /**
   * The cors properties.
   */
  @NestedConfigurationProperty
  private Cors cors;

  /**
   * The postgres properties.
   */
  @NestedConfigurationProperty
  private Postgres postgres;

  /**
   * Properties of rest.
   */
  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Rest {

    private Integer port;
  }

  /**
   * Properties of probes.
   */
  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Probes {

    private Integer port;
  }

  /**
   * Properties of cors.
   */
  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Cors {

    private String headers;
    private String origins;
  }

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
