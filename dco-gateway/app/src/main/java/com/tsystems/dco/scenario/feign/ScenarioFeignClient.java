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

package com.tsystems.dco.scenario.feign;

import com.tsystems.dco.scenario.model.Scenario;
import com.tsystems.dco.scenario.model.ScenarioPage;
import com.tsystems.dco.scenario.model.SimulationInput;
import com.tsystems.dco.scenario.model.SimulationPage;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.UUID;

@FeignClient(
  name = "scenario",
  url = "${app.scenario.rest.url}",
  configuration = FeignClientConfiguration.class
)
public interface ScenarioFeignClient {

  @PostMapping(value = "/api/scenario", produces = {"application/json"}, consumes = {"multipart/form-data"})
  ResponseEntity<Scenario> createScenario(@RequestPart(value = "scenario") String scenario,
                                          @RequestPart(value = "file") MultipartFile file);

  @DeleteMapping(value = "/api/scenario")
  ResponseEntity<Void> deleteScenarioById(@RequestParam(value = "id") UUID id);

  @GetMapping(value = "/api/scenario", produces = {"application/json"})
  ResponseEntity<ScenarioPage> scenarioReadByQuery(@RequestParam(value = "query") String query,
                                                   @RequestParam(value = "search") String search,
                                                   @RequestParam(value = "page") Integer page,
                                                   @RequestParam(value = "size") Integer size,
                                                   @RequestParam(value = "sort") List<String> sort);

  @PutMapping(value = "/api/scenario", produces = {"application/json"}, consumes = {"multipart/form-data"})
  ResponseEntity<Scenario> scenarioUpdateById(@RequestParam(value = "id") UUID id,
                                              @RequestPart(value = "scenario") String scenario,
                                              @RequestPart(value = "file") MultipartFile file);

  @GetMapping(value = "/api/scenario/search", produces = {"application/json"})
  ResponseEntity<ScenarioPage> searchScenarioByPattern(@RequestParam(value = "scenarioPattern") String scenarioPattern,
                                                       @RequestParam(value = "page") Integer page,
                                                       @RequestParam(value = "size") Integer size);

  @PostMapping(value = "/api/simulation", produces = {"application/json"}, consumes = {"application/json"})
  ResponseEntity<String> launchSimulation(@RequestBody SimulationInput simulationInput);

  @GetMapping(value = "/api/simulation", produces = {"application/json"})
  ResponseEntity<SimulationPage> simulationReadByQuery(@RequestParam(value = "query") String query,
                                                       @RequestParam(value = "search") String search,
                                                       @RequestParam(value = "page") Integer page,
                                                       @RequestParam(value = "size") Integer size,
                                                       @RequestParam(value = "sort") List<String> sort);
}
