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

import com.tsystems.dco.file.config.S3Config;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class FileStorageServiceTest {

  @InjectMocks
  private FileStorageServiceImpl fileStorageService;
  @Mock
  private MultipartFile file;
  @Mock
  private S3Config s3Config;

  private final String TEST = "TEST";

  @Test
  void getFilePath() throws NoSuchMethodException, SecurityException,
    IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    given(s3Config.getBucketName()).willReturn(TEST);
    given(s3Config.getMinioEndpoint()).willReturn("http://endpoint");
    Method method = FileStorageServiceImpl.class.getDeclaredMethod("getFilePath", String.class);
    method.setAccessible(true);
    assertEquals("http://endpoint/TEST/TEST", (String) method.invoke(fileStorageService, TEST));
  }

  @Test
  void createObjectNameForFile() throws NoSuchMethodException, SecurityException,
    IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    UUID uuid = UUID.randomUUID();
    Method method = FileStorageServiceImpl.class.getDeclaredMethod("createObjectNameForFile", UUID.class, String.class);
    method.setAccessible(true);
    assertEquals("scenario/" + uuid + "/files/TEST", (String) method.invoke(fileStorageService, uuid, TEST));
  }

  @Test
  void deleteFileWithError() {
    given(s3Config.getBucketName()).willReturn(TEST);
    assertThrows(NullPointerException.class, () -> fileStorageService.deleteFile(TEST));
  }
}
