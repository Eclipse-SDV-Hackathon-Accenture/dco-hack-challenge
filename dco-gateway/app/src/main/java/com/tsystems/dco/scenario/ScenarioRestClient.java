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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tsystems.dco.scenario.feign.ScenarioFeignClient;
import com.tsystems.dco.scenario.model.Scenario;
import com.tsystems.dco.scenario.model.ScenarioInput;
import com.tsystems.dco.scenario.model.ScenarioPage;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ScenarioRestClient implements ScenarioClient {

  private static final Logger LOGGER = LoggerFactory.getLogger(ScenarioRestClient.class);

  private final ScenarioFeignClient client;


  /**
   * @param scenarioInput
   * @param file
   * @return Scenario
   */
  @SneakyThrows
  @Override
  public Scenario createScenario(ScenarioInput scenarioInput, MultipartFile file) {
    var mapper = new ObjectMapper();
    String scenario = mapper.writeValueAsString(scenarioInput);
    ResponseEntity<Scenario> scenarioResponseEntity = client.createScenario(scenario, file);
    LOGGER.debug("Received response for createScenario : {}", scenarioResponseEntity.getStatusCode());
    return scenarioResponseEntity.getBody();
  }

  /**
   * @param id
   */
  @Override
  public void deleteScenarioById(UUID id) {
    client.deleteScenarioById(id);
  }

  /**
   * @param id
   * @param scenarioInput
   * @param file
   * @return Scenario
   */
  @SneakyThrows
  @Override
  public Scenario updateScenario(UUID id, ScenarioInput scenarioInput, MultipartFile file) {
    var mapper = new ObjectMapper();
    String scenario = mapper.writeValueAsString(scenarioInput);
    ResponseEntity<Scenario> scenarioResponseEntity = client.scenarioUpdateById(id, scenario, file);
    LOGGER.debug("Received response for updateScenario : {}", scenarioResponseEntity.getStatusCode());
    return scenarioResponseEntity.getBody();
  }

  /**
   * @param query
   * @param search
   * @param page
   * @param size
   * @param sort
   * @return ScenarioPage
   */
  @Override
  public ScenarioPage scenarioReadByQuery(String query, String search, Integer page, Integer size, List<String> sort) {
    ResponseEntity<ScenarioPage> scenarioPageResponseEntity = client.scenarioReadByQuery(query, search, page, size, sort);
    LOGGER.debug("Received response for scenarioReadByQuery : {}", scenarioPageResponseEntity.getStatusCode());
    return scenarioPageResponseEntity.getBody();
  }

  /**
   * @param scenarioPattern
   * @param page
   * @param size
   * @return ScenarioPage
   */
  @Override
  public ScenarioPage searchScenarioByPattern(String scenarioPattern, Integer page, Integer size) {
    ResponseEntity<ScenarioPage> scenarioPageResponseEntity = client.searchScenarioByPattern(scenarioPattern, page, size);
    LOGGER.debug("Received response for searchScenarioByPattern : {}", scenarioPageResponseEntity.getStatusCode());
    return scenarioPageResponseEntity.getBody();
  }
}
