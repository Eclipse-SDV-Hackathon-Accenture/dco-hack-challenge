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

package com.tsystems.dco.scenario.service;

import com.tsystems.dco.exception.DataDeletionException;
import com.tsystems.dco.exception.DataNotFoundException;
import com.tsystems.dco.file.entity.FileEntity;
import com.tsystems.dco.file.service.FileStorageService;
import com.tsystems.dco.model.FileData;
import com.tsystems.dco.model.Scenario;
import com.tsystems.dco.model.ScenarioPage;
import com.tsystems.dco.scenario.entity.ScenarioEntity;
import com.tsystems.dco.scenario.repository.ScenarioRepository;
import com.tsystems.dco.simulation.entity.SimulationEntity;
import com.tsystems.dco.simulation.repository.SimulationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ScenarioServiceTest {

  @InjectMocks
  private ScenarioServiceImpl scenarioService;
  @Mock
  private ScenarioRepository scenarioRepository;
  @Mock
  private FileStorageService fileStorageService;
  @Mock
  private PageRequest pageRequest;
  @Mock
  private SimulationRepository simulationRepository;
  @Mock
  private Page<ScenarioEntity> scenarioEntityPage;

  private final String TEST = "TEST";


  @Test
  void createScenario() {
    String scenarioInput = "{\"name\":\"TEST\",\"description\":\"TEST\",\"lastModifiedBy\":\"TEST\",\"createdBy\":\"TEST\"}";
    ScenarioEntity entity = ScenarioEntity.builder().name(TEST).description(TEST).createdBy(TEST).build();
    given(scenarioRepository.save(any())).willReturn(entity);
    given(fileStorageService.uploadFile(any(), any(), any())).willReturn(FileData.builder().build());
    MultipartFile file = new MockMultipartFile(TEST, TEST.getBytes());
    Scenario scenario = scenarioService.createScenario(scenarioInput, file);
    assertEquals(TEST, scenario.getName());
    verify(scenarioRepository, times(2)).save(any());
  }

  @Test
  void createScenarioWithException() {
    String scenarioInput = "{\"name\":\"TEST\",\"description\":\"TEST\",\"lastModifiedBy\":\"TEST\",\"createdBy\":\"TEST\"}";
    given(scenarioRepository.save(any())).willThrow(DataNotFoundException.class);
    MultipartFile file = new MockMultipartFile(TEST, TEST.getBytes());
    assertThrows(DataNotFoundException.class, () -> scenarioService.createScenario(scenarioInput, file));
  }

  @Test
  void readScenarioByQuery(){
    given(scenarioRepository.findAll(any(Specification.class), any(PageRequest.class))).willReturn(scenarioEntityPage);
    assertNotNull(scenarioService.readScenarioByQuery(TEST, TEST, 0, 10, null));
  }

  @Test
  void deleteScenarioById() {
    UUID uuid = UUID.randomUUID();
    ScenarioEntity scenarioEntity = ScenarioEntity.builder().name(TEST).description(TEST).createdBy(TEST).build();
    given(scenarioRepository.findById(uuid)).willReturn(Optional.of(scenarioEntity));
    List<UUID> scenarioIds = Arrays.asList(UUID.randomUUID(), uuid);
    List<SimulationEntity> simulationEntities = Arrays.asList(SimulationEntity.builder().id(UUID.randomUUID()).scenarios(scenarioIds).build());
    given(simulationRepository.findAll()).willReturn(simulationEntities);
    scenarioService.deleteScenarioById(uuid);
    verify(scenarioRepository).findById(any());
  }

  @Test
  void deleteScenarioByIdWithError() {
    UUID uuid = UUID.randomUUID();
    ScenarioEntity scenarioEntity = ScenarioEntity.builder().name(TEST).id(uuid).createdBy(TEST).build();
    given(scenarioRepository.findById(uuid)).willReturn(Optional.of(scenarioEntity));
    List<UUID> scenarioIds = Arrays.asList(uuid, UUID.randomUUID());
    List<SimulationEntity> simulationEntities = Arrays.asList(SimulationEntity.builder().scenarios(scenarioIds).build());
    given(simulationRepository.findAll()).willReturn(simulationEntities);
    assertThrows(DataDeletionException.class, () -> scenarioService.deleteScenarioById(uuid));
  }

  @Test
  void scenarioUpdateById() {
    UUID uuid = UUID.randomUUID();
    String scenarioInput = "{\"name\":\"TEST\",\"type\":\"MQTT\",\"lastModifiedBy\":\"TEST\",\"createdBy\":\"TEST\"}";
    FileEntity fileEntity = FileEntity.builder().fileKey(TEST).build();
    ScenarioEntity scenarioEntity = ScenarioEntity.builder().id(uuid).name(TEST).file(fileEntity).createdBy(TEST).build();
    given(scenarioRepository.findById(any())).willReturn(Optional.of(scenarioEntity));
    doNothing().when(fileStorageService).deleteFile(any());
    given(fileStorageService.uploadFile(any(), any(), any())).willReturn(FileData.builder().build());
    MultipartFile file = new MockMultipartFile(TEST, TEST.getBytes());
    scenarioService.scenarioUpdateById(uuid, scenarioInput, file);
    verify(scenarioRepository).save(any());
  }

  @Test
  void searchScenarioByPattern() {
    given(scenarioEntityPage.getPageable()).willReturn(pageRequest);
    given(scenarioEntityPage.getPageable().getPageNumber()).willReturn(1);
    given(scenarioEntityPage.getPageable().getPageSize()).willReturn(1);
    given(scenarioRepository.findScenarioByLike(anyString(), any())).willReturn(scenarioEntityPage);
    ScenarioPage scenarioPage = scenarioService.searchScenarioByPattern(TEST, 0, 10);
    assertEquals(1, scenarioPage.getPage());
    verify(scenarioRepository).findScenarioByLike(anyString(), any());
  }
}
