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

package com.tsystems.dco.track.service;

import com.tsystems.dco.exception.DataDeletionException;
import com.tsystems.dco.exception.DataNotFoundException;
import com.tsystems.dco.integration.ScenarioApiClient;
import com.tsystems.dco.model.Track;
import com.tsystems.dco.model.TrackInput;
import com.tsystems.dco.model.VehicleResponse;
import com.tsystems.dco.track.entity.TrackEntity;
import com.tsystems.dco.track.repository.TrackRepository;
import com.tsystems.dco.vehicle.entity.VehicleEntity;
import com.tsystems.dco.vehicle.service.VehicleService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class TrackServiceTest {

  @InjectMocks
  private TrackServiceImpl trackService;
  @Mock
  private PageRequest pageRequest;
  @Mock
  private TrackRepository trackRepository;
  @Mock
  private Page<TrackEntity> trackEntityPage;
  @Mock
  ResponseEntity<Boolean> booleanResponseEntity;
  @Mock
  private ScenarioApiClient scenarioApiClient;
  @Mock
  private Specification<TrackEntity> trackEntitySpecification;
  @Mock
  private VehicleService vehicleService;
  private final String TEST = "TEST";

  @Test
  void createTrack() {
    TrackInput trackInput = TrackInput.builder().name(TEST).build();
    given(trackRepository.save(any())).willReturn(TrackEntity.builder().name(TEST).build());
    Track scenario = trackService.createTrack(trackInput);
    assertEquals(TEST, scenario.getName());
    verify(trackRepository).save(any());
  }

  @Test
  void deleteTrackById() {
    TrackEntity trackEntity = TrackEntity.builder().name(TEST).build();
    given(trackRepository.findById(any())).willReturn(Optional.ofNullable(trackEntity));
    given(scenarioApiClient.isTrackAssociatedWithSimulation(any())).willReturn(booleanResponseEntity);
    given(booleanResponseEntity.getBody()).willReturn(false);
    trackService.deleteTrackById(UUID.randomUUID());
    verify(scenarioApiClient).isTrackAssociatedWithSimulation(any());
  }

  @Test
  void deleteTrackByIdWithError() {
    TrackEntity trackEntity = TrackEntity.builder().name(TEST).build();
    given(trackRepository.findById(any())).willReturn(Optional.ofNullable(trackEntity));
    given(scenarioApiClient.isTrackAssociatedWithSimulation(any())).willReturn(booleanResponseEntity);
    given(booleanResponseEntity.getBody()).willReturn(true);
    UUID uuid = UUID.randomUUID();
    assertThrows(DataDeletionException.class, () -> trackService.deleteTrackById(uuid));
  }

  @Test
  void readTrackByQuery() {
    given(trackRepository.findAll(any(Specification.class), any(PageRequest.class))).willReturn(trackEntityPage);
    trackService.readTrackByQuery(null, TEST, 0, 10, null);
  }

  @Test
  void findTrackById() {
    VehicleEntity vehicleEntity = VehicleEntity.builder().build();
    List<VehicleEntity> vehicleEntities = new ArrayList<>();
    vehicleEntities.add(vehicleEntity);
    TrackEntity trackEntity = TrackEntity.builder().name(TEST).vehicles(vehicleEntities).build();
    given(trackRepository.findById(any())).willReturn(Optional.ofNullable(trackEntity));
    given(vehicleService.getVehicleByVin(any())).willReturn(new VehicleResponse());
    assertEquals(TEST, trackService.findTrackById(UUID.randomUUID()).getName());
    verify(trackRepository).findById(any());
  }

  @Test
  void searchTrackByPattern() {
    given(trackEntityPage.getPageable()).willReturn(pageRequest);
    given(trackEntityPage.getPageable().getPageNumber()).willReturn(1);
    given(trackEntityPage.getPageable().getPageSize()).willReturn(1);
    given(trackRepository.findByTrackLike(any(), any())).willReturn(trackEntityPage);
    assertEquals(1, trackService.searchTrackByPattern(TEST, 0, 10).getSize());
    verify(trackRepository).findByTrackLike(any(), any());
  }

  @Test
  void findTrackByIds() {
    VehicleEntity vehicleEntity = VehicleEntity.builder().build();
    List<VehicleEntity> vehicleEntities = new ArrayList<>();
    vehicleEntities.add(vehicleEntity);
    TrackEntity trackEntity = TrackEntity.builder().name(TEST).vehicles(vehicleEntities).build();
    List<TrackEntity> trackEntities = new ArrayList<>();
    trackEntities.add(trackEntity);
    given(trackRepository.findTrackByIds(any())).willReturn(trackEntities);
    given(vehicleService.getVehicleByVin(any())).willReturn(new VehicleResponse());
    List<UUID> trackIds = List.of(UUID.randomUUID(), UUID.randomUUID());
    trackService.findTrackByIds(trackIds);
    verify(trackRepository).findTrackByIds(any());
  }

  @Test
  void isTracksExists() {
    List<UUID> trackIds = List.of(UUID.randomUUID(), UUID.randomUUID());
    given(trackRepository.existsById(any())).willReturn(true);
    assertTrue(trackService.isTracksExists(trackIds));
  }

  @Test
  void isTracksExistsWithError() {
    List<UUID> trackIds = List.of(UUID.randomUUID(), UUID.randomUUID());
    given(trackRepository.existsById(any())).willReturn(false);
    assertThrows(DataNotFoundException.class, () -> trackService.isTracksExists(trackIds));
  }

  @Test
  void getHardwareModule() {
    assertNotNull(trackService.getHardwareModule());
  }
}
