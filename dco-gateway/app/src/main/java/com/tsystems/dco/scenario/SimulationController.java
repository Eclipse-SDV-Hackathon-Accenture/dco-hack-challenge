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

import com.tsystems.dco.scenario.model.SimulationInput;
import com.tsystems.dco.scenario.model.SimulationPage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class SimulationController {

  private static final Logger LOGGER = LoggerFactory.getLogger(SimulationController.class);

  private final SimulationClient simulationClient;


  /**
   * @param simulationInput
   * @return String
   */
  @MutationMapping
  public String launchSimulation(@Argument SimulationInput simulationInput) {
    LOGGER.info("Simulation Input  : {}", simulationInput);
    return simulationClient.launchSimulation(simulationInput);
  }

  /**
   * @param query
   * @param search
   * @param page
   * @param size
   * @param sort
   * @return SimulationPage
   */
  @QueryMapping
  public SimulationPage simulationReadByQuery(@Argument String query, @Argument String search, @Argument Integer page, @Argument Integer size, @Argument List<String> sort) {
    LOGGER.info("simulation read by query");
    return simulationClient.simulationReadByQuery(query, search, page, size, sort);
  }

}
