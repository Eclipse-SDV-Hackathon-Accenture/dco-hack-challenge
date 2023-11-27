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

package com.tsystems.dco.file.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import java.net.URI;

@Data
@Configuration
public class S3Config {

  @Value("${app.storage.bucket}")
  private String bucketName;

  @Value("${app.storage.url}")
  private String minioEndpoint;

  @Value("${app.storage.access.key}")
  private String minioAccessKey;

  @Value("${app.storage.secret.key}")
  private String minioSecretKey;


  @Bean
  public S3Client s3Client() {
    return S3Client.builder()
      .endpointOverride(URI.create(minioEndpoint))
      .credentialsProvider(() -> AwsBasicCredentials.create(minioAccessKey, minioSecretKey))
      .region(Region.AWS_GLOBAL)
      .build();
  }
}
