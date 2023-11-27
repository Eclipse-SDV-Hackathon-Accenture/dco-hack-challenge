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

import com.tsystems.dco.scenario.model.SimulationPage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureGraphQlTester;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.graphql.test.tester.GraphQlTester;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureGraphQlTester
class SimulationControllerTest {

  @Autowired
  private GraphQlTester tester;

  @MockBean
  private SimulationRestClient client;

  @Test
  void launchSimulation() {
    var track = """
      mutation LAUNCH_SIMULATION {
        launchSimulation(
          simulationInput: {
            name: "Test Simulation"
            environment: "Development Demo"
            platform: "Task Management"
            scenarioType: "Over-The-Air Service"
            hardware: "TEST_OTA"
            description: "Test Simulation Data"
            tracks: ["bd6ed64b-0547-42b4-9769-d0a0ea095070", "5a955942-6169-4b0f-a26b-b4585d19ed55"]
          	scenarios: ["74fef2c0-4efc-42bb-b064-da7371421c8b"]
            createdBy: "Kashif.Kamal"
          }
        )
      }
      """;
    when(client.launchSimulation(any())).thenReturn("test");
    tester.document(track).execute().errors().satisfy(error -> assertThat(error.size() == 0));
  }

  @Test
  void simulationReadByQuery() {
    var track = """
      query LIST_SIMULATION {
        simulationReadByQuery(search: null, query: null, page: 0, size: 2, sort: null){
         content{
          id
        	name
        	status
          environment
        }
        total
       }
      } 
      """;
    when(client.simulationReadByQuery(any(), any(), anyInt(), anyInt(), any())).thenReturn(new SimulationPage());
    tester.document(track).execute().errors().satisfy(error -> assertThat(error.size() == 0));
  }
}
