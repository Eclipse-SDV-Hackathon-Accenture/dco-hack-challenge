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

package com.tsystems.dco.simulation.service;

import com.tsystems.dco.exception.DataNotFoundException;
import com.tsystems.dco.integration.*;
import com.tsystems.dco.mapper.SimulationMapper;
import com.tsystems.dco.model.Simulation;
import com.tsystems.dco.model.SimulationInput;
import com.tsystems.dco.model.SimulationPage;
import com.tsystems.dco.scenario.repository.ScenarioRepository;
import com.tsystems.dco.simulation.entity.SimulationEntity;
import com.tsystems.dco.simulation.repository.SimulationRepository;
import com.tsystems.dco.util.QueryUtil;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class SimulationServiceImpl implements SimulationService {

  private static final Logger LOGGER = LoggerFactory.getLogger(SimulationServiceImpl.class);

  private final SimulationRepository simulationRepository;
  private final TrackRepositoryApiClient trackRepositoryApiClient;
  private final CampaignService campaignService;
  private final ScenarioRepository scenarioRepository;


  /**
   * @param simulationInput
   * @return String
   */
  @Override
  public String launchSimulation(SimulationInput simulationInput) {
    isScenariosExists(simulationInput.getScenarios());
    isTracksExists(simulationInput.getTracks());
    SimulationEntity simulationEntity = SimulationMapper.INSTANCE.toEntity(simulationInput);
    //start campaign for the simulation
    var campaignRequest = CampaignRequest.builder().name(simulationInput.getName()).build();
    Campaign campaign = campaignService.startCampaign(campaignRequest);
    LOGGER.info("campaign Id {} for simulation {}", campaign.getId(), simulationInput.getName());
    simulationEntity.setCampaignId(campaign.getId());
    simulationEntity.setStatus(campaign.getStatus());
    SimulationEntity simulation = simulationRepository.save(simulationEntity);
    return "Simulation launched with id : " + simulation.getId();
  }

  /**
   * @param scenarios
   */
  private void isScenariosExists(List<UUID> scenarios) {
    scenarios.forEach(scenario -> {
      if (!scenarioRepository.existsById(scenario)) {
        throw new DataNotFoundException(HttpStatus.BAD_REQUEST, "Scenario doesn't exists");
      }
    });
  }

  /**
   * @param tracks
   */
  private void isTracksExists(List<UUID> tracks) {
    try {
      trackRepositoryApiClient.isTracksExists(tracks);
    } catch (FeignException.BadRequest e) {
      LOGGER.error("error {}", e.getMessage());
      throw new DataNotFoundException(HttpStatus.BAD_REQUEST, "Track doesn't exists");
    }
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
    var pageable = QueryUtil.getPageRequest(page, size, sort);
    var specs = SimulationQueryUtil.getSimulationQuerySpecification(query, search);
    var queried = simulationRepository.findAll(specs, pageable);

    List<Simulation> simulations = new ArrayList<>();
    List<SimulationEntity> simulationEntities = queried.getContent();
    simulationEntities.forEach(simulation -> {
      List<UUID> trackIds = simulation.getTracks();
      List<Track> tracks = trackRepositoryApiClient.findTrackByIds(trackIds).getBody();
      IntStream vehicleStream = tracks.stream().mapToInt(v -> v.getVehicles().size());
      List<String> brands = tracks.stream().flatMap(track -> track.getVehicles().stream().map(VehicleResponse::getBrand)).collect(Collectors.toList());
      Simulation sim = Simulation.builder().id(simulation.getId())
        .name(simulation.getName())
        .platform(simulation.getPlatform())
        .environment(simulation.getEnvironment())
        .scenarioType(simulation.getScenarioType())
        .startDate(simulation.getStartDate())
        .status(campaignService.checkStatus(simulation.getCampaignId()).getStatus())
        .noOfVehicle(vehicleStream.sum())
        .brands(brands)
        .hardware(simulation.getHardware())
        .noOfScenarios(simulation.getScenarios().size())
        .createdBy(simulation.getCreatedBy())
        .description(simulation.getDescription())
        .build();
      simulations.add(sim);
    });

    return new SimulationPage()
      .empty(queried.isEmpty())
      .first(queried.isFirst())
      .last(queried.isLast())
      .page(queried.getNumber())
      .size(queried.getSize())
      .pages(queried.getTotalPages())
      .elements(queried.getNumberOfElements())
      .total(queried.getTotalElements())
      .content(simulations);
  }

  /**
   * @param trackId
   * @return boolean
   */
  @Override
  public boolean isTrackAssociatedWithSimulation(UUID trackId) {
    List<SimulationEntity> simulationEntities = simulationRepository.findAll();
    List<SimulationEntity> matchedData = simulationEntities.stream().filter(sim -> sim.getTracks().stream()
      .anyMatch(t -> t.equals(trackId))).collect(Collectors.toList());
    return !matchedData.isEmpty();
  }

}
