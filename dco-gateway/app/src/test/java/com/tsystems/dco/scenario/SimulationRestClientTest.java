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
import com.tsystems.dco.scenario.model.SimulationInput;
import com.tsystems.dco.scenario.model.SimulationPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ContextConfiguration(classes = {SimulationRestClient.class})
@ExtendWith(SpringExtension.class)
class SimulationRestClientTest {

  @Autowired
  private SimulationRestClient simulationRestClient;
  @MockBean
  private ScenarioFeignClient client;
  @Mock
  private ResponseEntity<SimulationPage> simulationPageResponseEntity;
  @Mock
  private ResponseEntity<String> responseEntity;


  @Test
  void launchSimulation() {
    SimulationInput simulationInput = new SimulationInput();
    given(responseEntity.getBody()).willReturn("test");
    given(client.launchSimulation(any())).willReturn(responseEntity);
    assertSame("test", simulationRestClient.launchSimulation(new SimulationInput()));
    verify(client).launchSimulation(any());
    verify(responseEntity).getBody();
  }

  @Test
  void simulationReadByQuery() {
    SimulationPage simulationPage = new SimulationPage();
    given(simulationPageResponseEntity.getBody()).willReturn(simulationPage);
    given(client.simulationReadByQuery(any(), any(), any(), any(), any())).willReturn(simulationPageResponseEntity);
    assertSame(simulationPage, simulationRestClient.simulationReadByQuery(null, null, 0, 10, null));
    verify(client).simulationReadByQuery(any(), any(), any(), any(), any());
    verify(simulationPageResponseEntity).getBody();
  }
}
