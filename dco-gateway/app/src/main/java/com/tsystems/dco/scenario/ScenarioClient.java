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
import com.tsystems.dco.scenario.model.ScenarioPage;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.UUID;

public interface ScenarioClient {

  Scenario createScenario(ScenarioInput scenarioInput, MultipartFile file);

  void deleteScenarioById(UUID id);

  Scenario updateScenario(UUID id, ScenarioInput scenarioInput, MultipartFile file);

  ScenarioPage scenarioReadByQuery(String query, String search, Integer page, Integer size, List<String> sort);

  ScenarioPage searchScenarioByPattern(String scenarioPattern, Integer page, Integer size);
}
