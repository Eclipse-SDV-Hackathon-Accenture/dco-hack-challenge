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

package com.tsystems.dco.scenario;

import com.tsystems.dco.scenario.feign.ScenarioFeignClient;
import com.tsystems.dco.scenario.model.Scenario;
import com.tsystems.dco.scenario.model.ScenarioInput;
import com.tsystems.dco.scenario.model.ScenarioPage;
import feign.FeignException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.verify;

@ContextConfiguration(classes = {ScenarioRestClient.class})
@ExtendWith(SpringExtension.class)
class ScenarioRestClientTest {

  @Autowired
  private ScenarioRestClient scenarioRestClient;
  @MockBean
  private ScenarioFeignClient client;
  @Mock
  private ResponseEntity<ScenarioPage> scenarioPageResponseEntity;
  @Mock
  private ResponseEntity<Scenario> scenarioResponseEntity;
  private final static String TEST = "Test";

  @Test
  void createScenario() throws IOException {
    InputStream is = new ByteArrayInputStream(TEST.getBytes());
    MultipartFile file = new MockMultipartFile(TEST, is);
    ScenarioInput scenarioInput = new ScenarioInput();
    Scenario scenario = new Scenario();
    given(scenarioResponseEntity.getBody()).willReturn(scenario);
    given(client.createScenario(any(), any())).willReturn(scenarioResponseEntity);
    assertSame(scenario, scenarioRestClient.createScenario(scenarioInput, file));
    verify(client).createScenario(any(), any());
    verify(scenarioResponseEntity).getBody();
  }

  @Test
  void createScenarioWithError() throws IOException {
    InputStream is = new ByteArrayInputStream(TEST.getBytes());
    MultipartFile file = new MockMultipartFile(TEST, is);
    ScenarioInput scenarioInput = new ScenarioInput();
    Scenario scenario = new Scenario();
    given(scenarioResponseEntity.getBody()).willThrow(FeignException.class);
    given(client.createScenario(any(), any())).willReturn(scenarioResponseEntity);
    assertThrows(FeignException.class, () -> scenarioRestClient.createScenario(scenarioInput, file));
  }
  @Test
  void deleteScenarioById() {
    scenarioRestClient.deleteScenarioById(UUID.randomUUID());
    verify(client).deleteScenarioById(any());
  }

  @Test
  void updateScenario() throws IOException {
    InputStream is = new ByteArrayInputStream(TEST.getBytes());
    MultipartFile file = new MockMultipartFile(TEST, is);
    ScenarioInput scenarioInput = new ScenarioInput();
    Scenario scenario = new Scenario();
    given(scenarioResponseEntity.getBody()).willReturn(scenario);
    given(client.scenarioUpdateById(any(), any(), any())).willReturn(scenarioResponseEntity);
    assertSame(scenario, scenarioRestClient.updateScenario(UUID.randomUUID(), scenarioInput, file));
    verify(client).scenarioUpdateById(any(), any(), any());
    verify(scenarioResponseEntity).getBody();
  }

  @Test
  void updateScenarioWithError() throws IOException {
    InputStream is = new ByteArrayInputStream(TEST.getBytes());
    MultipartFile file = new MockMultipartFile(TEST, is);
    ScenarioInput scenarioInput = new ScenarioInput();
    Scenario scenario = new Scenario();
    given(scenarioResponseEntity.getBody()).willReturn(scenario);
    given(client.scenarioUpdateById(any(), any(), any())).willThrow(FeignException.class);
    UUID uuid = UUID.randomUUID();
    assertThrows(FeignException.class, () -> scenarioRestClient.updateScenario(uuid, scenarioInput, file));
  }

  @Test
  void scenarioReadByQuery() {
    ScenarioPage scenarioPage = new ScenarioPage();
    given(scenarioPageResponseEntity.getBody()).willReturn(scenarioPage);
    given(client.scenarioReadByQuery(any(), any(), any(), any(), any())).willReturn(scenarioPageResponseEntity);
    assertSame(scenarioPage, scenarioRestClient.scenarioReadByQuery(null, null, 0, 10, null));
    verify(client).scenarioReadByQuery(any(), any(), any(), any(), any());
    verify(scenarioPageResponseEntity).getBody();
  }

  @Test
  void searchScenarioByPattern() {
    ScenarioPage scenarioPage = new ScenarioPage();
    given(scenarioPageResponseEntity.getBody()).willReturn(scenarioPage);
    given(client.searchScenarioByPattern(anyString(), anyInt(), anyInt())).willReturn(scenarioPageResponseEntity);
    assertSame(scenarioPage, scenarioRestClient.searchScenarioByPattern(TEST, 0, 10));
    verify(client).searchScenarioByPattern(anyString(), anyInt(), anyInt());
    verify(scenarioPageResponseEntity).getBody();
  }

  @Test
  void searchScenarioByPatternWithException() {
    given(scenarioPageResponseEntity.getBody()).willThrow(FeignException.InternalServerError.class);
    given(client.searchScenarioByPattern(anyString(), anyInt(), anyInt())).willReturn(scenarioPageResponseEntity);
    assertThrows(FeignException.InternalServerError.class, () -> scenarioRestClient.searchScenarioByPattern(TEST, 0, 10));
  }
}
