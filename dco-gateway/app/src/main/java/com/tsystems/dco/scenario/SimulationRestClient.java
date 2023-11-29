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
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SimulationRestClient implements SimulationClient {

  private final ScenarioFeignClient client;


  /**
   * @param simulationInput
   * @return String
   */
  @Override
  public String launchSimulation(SimulationInput simulationInput) {
    ResponseEntity<String> responseEntity = client.launchSimulation(simulationInput);
    return responseEntity.getBody();
  }

  /**
   * @param query
   * @param search
   * @param page
   * @param size
   * @param sort
   * @return SimulationPage
   */
  @Override
  public SimulationPage simulationReadByQuery(String query, String search, Integer page, Integer size, List<String> sort) {
    ResponseEntity<SimulationPage> simulationPageResponseEntity = client.simulationReadByQuery(query, search, page, size, sort);
    return simulationPageResponseEntity.getBody();
  }
}
