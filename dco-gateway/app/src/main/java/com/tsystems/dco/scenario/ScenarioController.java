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

import com.tsystems.dco.scenario.model.ScenarioInput;
import com.tsystems.dco.scenario.model.ScenarioPage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class ScenarioController {

  private static final Logger LOGGER = LoggerFactory.getLogger(ScenarioController.class);

  private final ScenarioClient scenarioClient;


  /**
   * @param file
   * @param scenarioInput
   * @return UUID
   */
  @MutationMapping
  public UUID createScenario(@Argument MultipartFile file, @Argument ScenarioInput scenarioInput) {
    LOGGER.info("Scenario  : {}", scenarioInput);
    return scenarioClient.createScenario(scenarioInput, file).getId();
  }


  /**
   * @param query
   * @param search
   * @param page
   * @param size
   * @param sort
   * @return ScenarioPage
   */
  @QueryMapping
  public ScenarioPage scenarioReadByQuery(@Argument String query, @Argument String search, @Argument Integer page, @Argument Integer size, @Argument List<String> sort) {
    LOGGER.info("Scenario read ");
    return scenarioClient.scenarioReadByQuery(query, search, page, size, sort);
  }


  /**
   * @return String
   */
  @MutationMapping
  public String deleteScenarioById(@Argument UUID id) {
    LOGGER.info("Scenario id : {}", id);
    scenarioClient.deleteScenarioById(id);
    return "Scenario deleted - " + id;
  }

  /**
   * @return UUID
   */
  @MutationMapping
  public UUID updateScenario(@Argument UUID id, @Argument MultipartFile file, @Argument ScenarioInput scenarioInput) {
    LOGGER.info("Scenario  : {}", scenarioInput);
    return scenarioClient.updateScenario(id, scenarioInput, file).getId();
  }

  /**
   * @return ScenarioPage
   */
  @QueryMapping
  public ScenarioPage searchScenarioByPattern(@Argument String scenarioPattern, @Argument Integer page, @Argument Integer size) {
    LOGGER.info("Scenario search by pattern : {}", scenarioPattern);
    return scenarioClient.searchScenarioByPattern(scenarioPattern, page, size);
  }

}
