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
import com.tsystems.dco.integration.Campaign;
import com.tsystems.dco.integration.Track;
import com.tsystems.dco.integration.TrackRepositoryApiClient;
import com.tsystems.dco.integration.VehicleResponse;
import com.tsystems.dco.model.SimulationInput;
import com.tsystems.dco.scenario.repository.ScenarioRepository;
import com.tsystems.dco.simulation.entity.SimulationEntity;
import com.tsystems.dco.simulation.repository.SimulationRepository;
import feign.FeignException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SimulationServiceTest {

  @InjectMocks
  private SimulationServiceImpl simulationService;
  @Mock
  private ScenarioRepository scenarioRepository;
  @Mock
  private SimulationRepository simulationRepository;
  @Mock
  private CampaignService campaignService;
  @Mock
  private TrackRepositoryApiClient trackRepositoryApiClient;
  @Mock
  private Page<SimulationEntity> page;
  @Mock
  private ResponseEntity<List<Track>> trackResponseEntity;
  private final String TEST = "TEST";

  @Test
  void launchSimulation() {
    List<UUID> uuids = Arrays.asList(UUID.randomUUID(), UUID.randomUUID());
    given(scenarioRepository.existsById(any())).willReturn(true);
    given(campaignService.startCampaign(any())).willReturn(Campaign.builder().build());
    UUID uuid = UUID.randomUUID();
    SimulationEntity simulation = SimulationEntity.builder().id(uuid).name(TEST).description(TEST).createdBy(TEST).environment(TEST).hardware(TEST).build();
    given(simulationRepository.save(any())).willReturn(simulation);
    SimulationInput simulationInput = SimulationInput.builder().scenarios(uuids).tracks(uuids).build();
    assertEquals("Simulation launched with id : " + uuid, simulationService.launchSimulation(simulationInput));
    verify(campaignService).startCampaign(any());
  }

  @Test
  void launchSimulationWithScenarioError() {
    List<UUID> uuids = Arrays.asList(UUID.randomUUID(), UUID.randomUUID());
    given(scenarioRepository.existsById(any())).willReturn(false);
    SimulationInput simulationInput = SimulationInput.builder().scenarios(uuids).tracks(uuids).build();
    assertThrows(DataNotFoundException.class, () -> simulationService.launchSimulation(simulationInput));
  }

  @Test
  void launchSimulationWithTrackError() {
    List<UUID> uuids = Arrays.asList(UUID.randomUUID(), UUID.randomUUID());
    given(scenarioRepository.existsById(any())).willReturn(true);
    given(trackRepositoryApiClient.isTracksExists(uuids)).willThrow(FeignException.BadRequest.class);
    SimulationInput simulationInput = SimulationInput.builder().scenarios(uuids).tracks(uuids).build();
    assertThrows(DataNotFoundException.class, () -> simulationService.launchSimulation(simulationInput));
  }

  @Test
  void simulationReadByQueryWithError() {
    List<String> sort = new ArrayList<>();
    sort.add("name:asc");
    assertThrows(NullPointerException.class, () -> simulationService.simulationReadByQuery(null, TEST, 0, 10, sort));
  }

  @Test
  void simulationReadByQuery() {
    UUID uuid = UUID.randomUUID();
    SimulationEntity simulation = SimulationEntity.builder().id(uuid).name(TEST).description(TEST)
      .createdBy(TEST)
      .environment(TEST)
      .scenarios(Arrays.asList(UUID.randomUUID(), uuid))
      .hardware(TEST).build();
    List<SimulationEntity> simulationEntities = new ArrayList<>();
    simulationEntities.add(simulation);
    given(page.getContent()).willReturn(simulationEntities);
    given(simulationRepository.findAll(any(Specification.class), any(PageRequest.class))).willReturn(page);
    VehicleResponse vehicleResponse = new VehicleResponse();
    vehicleResponse.setVin(TEST);
    vehicleResponse.setBrand(TEST);
    vehicleResponse.setCountry(TEST);
    vehicleResponse.setModel(TEST);
    List<VehicleResponse> vehicleResponses = new ArrayList<>();
    vehicleResponses.add(vehicleResponse);
    Track track = new Track();
    track.setId(uuid);
    track.setName(TEST);
    track.setVehicles(vehicleResponses);
    List<Track> tracks = new ArrayList<>();
    tracks.add(track);
    given(trackResponseEntity.getBody()).willReturn(tracks);
    given(trackRepositoryApiClient.findTrackByIds(any())).willReturn(trackResponseEntity);
    Campaign campaign = Campaign.builder().status(TEST).id(uuid).build();
    given(campaignService.checkStatus(any())).willReturn(campaign);
    simulationService.simulationReadByQuery(null, TEST, 0, 10, null);
  }

  @Test
  void isTrackAssociatedWithSimulation() {
    UUID uuid = UUID.randomUUID();
    List<UUID> uuids = Arrays.asList(uuid, UUID.randomUUID());
    SimulationEntity simulationEntity = SimulationEntity.builder().tracks(uuids).build();
    List<SimulationEntity> simulationEntities = new ArrayList<>();
    simulationEntities.add(simulationEntity);
    given(simulationRepository.findAll()).willReturn(simulationEntities);
    simulationService.isTrackAssociatedWithSimulation(UUID.randomUUID());
  }
}
