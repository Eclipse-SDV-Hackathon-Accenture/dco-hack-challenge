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

package com.tsystems.dco.simulation.controller;

import com.tsystems.dco.api.SimulationApi;
import com.tsystems.dco.model.SimulationInput;
import com.tsystems.dco.model.SimulationPage;
import com.tsystems.dco.simulation.service.SimulationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class SimulationController implements SimulationApi {

  private static final Logger LOGGER = LoggerFactory.getLogger(SimulationController.class);
  private final SimulationService simulationService;

  /**
   * POST /api/simulation/launch : Launch Simulation
   * Launch a Simulation
   *
   * @param simulationInput (required)
   * @return OK (status code 200)
   * or Bad Request (status code 400)
   * or Not Found (status code 404)
   */
  @Override
  public ResponseEntity<String> launchSimulation(SimulationInput simulationInput) {
    LOGGER.info("Launching Simulation - {}", simulationInput);
    return ResponseEntity
      .status(HttpStatus.CREATED)
      .body(simulationService.launchSimulation(simulationInput));
  }

  /**
   * GET /api/simulation : Read Simulation by query
   * Read Simulation by query and pageable from database
   *
   * @param query  Comma separated list of &#x60;{field}{operation}{value}&#x60; where operation can be  &#x60;:&#x60; for equal,  &#x60;!&#x60; for not equal and  &#x60;~&#x60; for like operation (optional)
   * @param search Search value to query searchable fields against (optional)
   * @param page   (optional)
   * @param size   (optional)
   * @param sort   (optional)
   * @return OK (status code 200)
   * or Bad Request (status code 400)
   */
  @Override
  public ResponseEntity<SimulationPage> simulationReadByQuery(String query, String search, Integer page, Integer size, List<String> sort) {
    LOGGER.info("Simulation read for size - {}", size);
    return ResponseEntity
      .status(HttpStatus.OK)
      .body(simulationService.simulationReadByQuery(query, search, page, size, sort));
  }

  /**
   * GET /api/simulation/track : is track associated with simulation
   * is track associated with simulation
   *
   * @param id The track id (required)
   * @return OK (status code 200)
   * or Bad Request (status code 400)
   * or Not Found (status code 404)
   */
  @Override
  public ResponseEntity<Boolean> isTrackAssociatedWithSimulation(UUID id) {
    LOGGER.info("check track id is associated with simulation : {}", id);
    return ResponseEntity
      .status(HttpStatus.OK)
      .body(simulationService.isTrackAssociatedWithSimulation(id));
  }
}
