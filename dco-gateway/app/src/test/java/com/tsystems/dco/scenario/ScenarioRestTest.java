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

import com.tsystems.dco.scenario.model.Scenario;
import com.tsystems.dco.scenario.model.ScenarioInput;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ScenarioRestTest {

  @InjectMocks
  private ScenarioController scenarioController;
  @Mock
  private ScenarioRestClient scenarioRestClient;
  private final static String TEST = "Test";

  @Test
  void createScenario() throws IOException {
    UUID id = UUID.randomUUID();
    Scenario scenario = Scenario.builder().id(id).build();
    given(scenarioRestClient.createScenario(any(), any())).willReturn(scenario);
    ScenarioInput scenarioInput = new ScenarioInput();
    Assertions.assertEquals(id, scenarioController.createScenario(getFile(), scenarioInput));
  }

  @Test
  void updateScenario() throws IOException {
    UUID id = UUID.randomUUID();
    Scenario scenario = Scenario.builder().id(id).build();
    given(scenarioRestClient.updateScenario(any(), any(), any())).willReturn(scenario);
    ScenarioInput scenarioInput = new ScenarioInput();
    Assertions.assertEquals(id, scenarioController.updateScenario(id, getFile(), scenarioInput));
  }

  private MultipartFile getFile() throws IOException {
    InputStream is = new ByteArrayInputStream(TEST.getBytes());
    return new MockMultipartFile(TEST, is);
  }
}
