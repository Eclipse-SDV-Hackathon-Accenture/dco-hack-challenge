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

package com.tsystems.dco.file.service;

import com.google.common.io.BaseEncoding;
import com.tsystems.dco.file.config.S3Config;
import com.tsystems.dco.model.FileData;
import com.tsystems.dco.util.FileUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.checksums.Md5Checksum;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Utilities;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.UUID;
import java.util.zip.CheckedInputStream;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileStorageServiceImpl implements FileStorageService {

  private final S3Config s3Config;
  private final S3Client s3Client;

  /**
   * @param scenarioId
   * @param file
   * @param updatedBy
   * @return FileData
   */
  @SneakyThrows
  @Override
  public FileData uploadFile(UUID scenarioId, MultipartFile file, String updatedBy) {
    String fileName = file.getOriginalFilename();
    if(!FileUtil.validateFilename(fileName)) {
      throw new FileUploadException("File name is not valid");
    }

    long fileSize = file.getSize();
    String objectName = createObjectNameForFile(scenarioId, fileName);
    log.info("S3 object name : {}", objectName);
    if(objectName.getBytes().length > 1024) {
      throw new FileUploadException("File name length is too long for storing it in S3");
    }
    var checksum = new Md5Checksum();
    var checkedInputStream = new CheckedInputStream(file.getInputStream(), checksum);

    s3Client.putObject(PutObjectRequest.builder()
        .bucket(s3Config.getBucketName())
        .key(objectName)
        .build(),
      RequestBody.fromInputStream(checkedInputStream, fileSize));

    String filePath = getFilePath(objectName);
    log.info("Uploaded file to {}", filePath);

    String checksumStr = BaseEncoding.base16().encode(checksum.getChecksumBytes());
    return FileData
      .builder()
      .path(filePath)
      .fileKey(objectName)
      .checksum(checksumStr)
      .size(String.valueOf(fileSize))
      .updatedBy(updatedBy)
      .updatedOn(Instant.now())
      .build();
  }


  /**
   * @param scenarioId
   * @param fileName
   * @return String
   */
  private String createObjectNameForFile(UUID scenarioId, String fileName) {
    var builder = new StringBuilder();
    builder.append("scenario/").append(scenarioId).append("/files/").append(fileName);
    return builder.toString();
  }


  /**
   * @param objectName
   * @return String
   */
  private String getFilePath(String objectName) {
    var s3Utilities = S3Utilities.builder()
      .region(Region.AWS_GLOBAL)
      .build();
    var fileUrl = s3Utilities.getUrl(GetUrlRequest.builder()
        .endpoint(URI.create(s3Config.getMinioEndpoint()))
        .bucket(s3Config.getBucketName())
        .key(objectName)
        .build());
    return URLDecoder.decode(fileUrl.toString(), StandardCharsets.UTF_8);
  }

  /**
   * @param key
   */
  @Override
  public void deleteFile(String key) {
    DeleteObjectRequest del = DeleteObjectRequest.builder()
      .bucket(s3Config.getBucketName()).key(key).build();
    s3Client.deleteObject(del);
  }
}
