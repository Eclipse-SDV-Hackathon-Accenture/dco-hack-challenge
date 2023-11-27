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

package com.tsystems.dco.scenario.controller;

import com.tsystems.dco.model.ScenarioPage;
import com.tsystems.dco.scenario.service.ScenarioService;
import com.tsystems.dco.api.ScenarioApi;
import com.tsystems.dco.model.Scenario;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class ScenarioController implements ScenarioApi {

  private static final Logger LOGGER = LoggerFactory.getLogger(ScenarioController.class);

  private final ScenarioService scenarioService;

  /**
   * POST /api/scenario : Create a Scenario
   * Create a Scenario to database
   *
   * @param scenario (optional)
   * @param file     (optional)
   * @return Created (status code 201)
   * or Bad Request (status code 400)
   */
  @Override
  public ResponseEntity<Scenario> createScenario(String scenario, MultipartFile file) {
    LOGGER.info("Creating Scenario - {}", scenario);
    return ResponseEntity
      .status(HttpStatus.CREATED)
      .body(scenarioService.createScenario(scenario, file));
  }

  /**
   * DELETE /api/scenario : Delete Scenario by id
   * Delete Scenario by id from database
   *
   * @param id The scenario id (required)
   * @return No Content (status code 204)
   * or Not Found (status code 404)
   */
  @Override
  public ResponseEntity<Void> deleteScenarioById(UUID id) {
    LOGGER.info("Deleting scenario id - {}", id);
    scenarioService.deleteScenarioById(id);
    return ResponseEntity
      .status(HttpStatus.NO_CONTENT)
      .build();
  }

  /**
   * GET /api/scenario : Read Scenario by query
   * Read Scenario by query and pageable from database
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
  public ResponseEntity<ScenarioPage> scenarioReadByQuery(String query, String search, Integer page, Integer size, List<String> sort) {
    return ResponseEntity
      .status(HttpStatus.OK)
      .body(scenarioService.readScenarioByQuery(query, search, page, size, sort));
  }

  /**
   * PUT /api/scenario : Update Scenario by id
   * Update Scenario by id to database
   *
   * @param id       The scenario id (required)
   * @param scenario (optional)
   * @param file     (optional)
   * @return OK (status code 200)
   * or Bad Request (status code 400)
   * or Not Found (status code 404)
   */
  @Override
  public ResponseEntity<Scenario> scenarioUpdateById(UUID id, String scenario, MultipartFile file) {
    return ResponseEntity
      .status(HttpStatus.OK)
      .body(scenarioService.scenarioUpdateById(id, scenario, file));
  }

  /**
   * GET /api/scenario/search : Search for scenario with given &#39;%&#39; pattern. Returns paginated list
   * Search for scenario with given &#39;%&#39; pattern. Returns paginated list
   *
   * @param scenarioPattern (required)
   * @param page            (optional)
   * @param size            (optional)
   * @return OK (status code 200)
   * or Not Found (status code 404)
   */
  @Override
  public ResponseEntity<ScenarioPage> searchScenarioByPattern(String scenarioPattern, Integer page, Integer size) {
    LOGGER.info("searching scenario by pattern : {}", scenarioPattern);
    return ResponseEntity
      .status(HttpStatus.OK)
      .body(scenarioService.searchScenarioByPattern(scenarioPattern, page, size));
  }
}
