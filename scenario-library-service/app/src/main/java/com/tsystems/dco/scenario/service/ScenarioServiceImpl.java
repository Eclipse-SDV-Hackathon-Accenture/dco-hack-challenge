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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tsystems.dco.exception.DataDeletionException;
import com.tsystems.dco.exception.DataNotFoundException;
import com.tsystems.dco.file.entity.FileEntity;
import com.tsystems.dco.file.service.FileStorageService;
import com.tsystems.dco.mapper.ScenarioMapper;
import com.tsystems.dco.model.FileData;
import com.tsystems.dco.model.Scenario;
import com.tsystems.dco.model.ScenarioInput;
import com.tsystems.dco.model.ScenarioPage;
import com.tsystems.dco.scenario.entity.ScenarioEntity;
import com.tsystems.dco.scenario.repository.ScenarioRepository;
import com.tsystems.dco.simulation.entity.SimulationEntity;
import com.tsystems.dco.simulation.repository.SimulationRepository;
import com.tsystems.dco.util.QueryUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.transaction.Transactional;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScenarioServiceImpl implements ScenarioService {

  private static final Logger LOGGER = LoggerFactory.getLogger(ScenarioServiceImpl.class);
  private static final String ERROR_NOT_FOUND_ID = "Scenario with id %s not found.";
  private static final Sort.Order ORDER_DEFAULT = Sort.Order.asc("createdAt");
  private static final Integer PAGEABLE_DEFAULT_PAGE = 0;
  private static final Integer PAGEABLE_DEFAULT_SIZE = 15;
  private final ScenarioRepository scenarioRepository;
  private final FileStorageService fileStorageService;
  private final SimulationRepository simulationRepository;


  /**
   * @param scenarioInput
   * @param file
   * @return Scenario
   */
  @SneakyThrows
  @Transactional
  @Override
  public Scenario createScenario(String scenarioInput, MultipartFile file) {
    var mapper = new ObjectMapper();
    ScenarioInput scenario = mapper.readValue(scenarioInput, ScenarioInput.class);
    var scenarioEntity = ScenarioMapper.INSTANCE.toEntity(scenario);
    scenarioEntity = scenarioRepository.save(scenarioEntity);
    //file upload
    FileData fileData = fileStorageService.uploadFile(scenarioEntity.getId(), file, scenarioEntity.getCreatedBy());
    attachFile(scenarioEntity, fileData);
    return ScenarioMapper.INSTANCE.toModel(scenarioEntity);
  }


  /**
   * @param scenarioEntity
   * @param fileData
   */
  private void attachFile(ScenarioEntity scenarioEntity, FileData fileData) {
    FileEntity fileEntity = ScenarioMapper.INSTANCE.toEntity(fileData);
    scenarioEntity.setFile(fileEntity);
    scenarioRepository.save(scenarioEntity);
    LOGGER.info("Attached file to scenario : {}", scenarioEntity.getName());
  }


  /**
   * @param id
   */
  @Transactional
  @Override
  public void deleteScenarioById(UUID id) {
    //update scenario status to archived
    final var actual = scenarioRepository.findById(id)
      .orElseThrow(() -> new DataNotFoundException(HttpStatus.NOT_FOUND, String.format(ERROR_NOT_FOUND_ID, id)));
    //check, if associated with simulation
    boolean isScenarioAssociated = isScenarioAssociatedWithSimulation(actual.getId());
    if (isScenarioAssociated) {
      LOGGER.info("Deleting scenario : {}", actual.getId());
      actual.setStatus(ScenarioInput.StatusEnum.ARCHIVED.getValue());
    } else {
      LOGGER.error("Scenario id : {}, can't be deleted as it has an association with simulation", actual.getId());
      throw new DataDeletionException(HttpStatus.BAD_REQUEST, "Scenario can't be deleted as it has an association with simulation");
    }

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
  public ScenarioPage readScenarioByQuery(String query, String search, Integer page, Integer size, List<String> sort) {
    var pageable = QueryUtil.getPageRequest(page, size, sort);
    var specs = ScenarioQueryUtil.getScenarioQuerySpecification(query, search);
    var queried = scenarioRepository.findAll(specs, pageable);
    return new ScenarioPage()
      .empty(queried.isEmpty())
      .first(queried.isFirst())
      .last(queried.isLast())
      .page(queried.getNumber())
      .size(queried.getSize())
      .pages(queried.getTotalPages())
      .elements(queried.getNumberOfElements())
      .total(queried.getTotalElements())
      .content(ScenarioMapper.INSTANCE.toModel(queried.getContent()));
  }


  /**
   * @param id
   * @param scenarioInput
   * @param file
   * @return Scenario
   */
  @SneakyThrows
  @Transactional
  @Override
  public Scenario scenarioUpdateById(UUID id, String scenarioInput, MultipartFile file) {
    var mapper = new ObjectMapper();
    ScenarioInput scenario = mapper.readValue(scenarioInput, ScenarioInput.class);
    final var actualScenario = scenarioRepository.findById(id)
      .orElseThrow(() -> new DataNotFoundException(HttpStatus.NOT_FOUND, String.format(ERROR_NOT_FOUND_ID, id)));
    //update file
    var fileEntity = actualScenario.getFile();
    if (file != null && !file.isEmpty()) {
      FileData fileData = updateFile(id, fileEntity.getFileKey(), file, scenario.getLastModifiedBy());
      fileEntity.setUpdatedBy(fileData.getUpdatedBy());
      fileEntity.setFileKey(fileData.getFileKey());
      fileEntity.setChecksum(fileData.getChecksum());
      fileEntity.setSize(fileData.getSize());
      fileEntity.setPath(fileData.getPath());
      fileEntity.setUpdatedOn(fileData.getUpdatedOn());
    }
    actualScenario.setName(scenario.getName());
    actualScenario.setType(scenario.getType().getValue());
    actualScenario.setDescription(scenario.getDescription());
    actualScenario.setLastModifiedBy(scenario.getLastModifiedBy());
    actualScenario.setLastModifiedAt(Instant.now());
    actualScenario.setFile(fileEntity);
    final var updatedScenario = scenarioRepository.save(actualScenario);
    return ScenarioMapper.INSTANCE.toModel(updatedScenario);
  }


  /**
   * @param scenarioPattern
   * @param page
   * @param size
   * @return ScenarioPage
   */
  @Override
  public ScenarioPage searchScenarioByPattern(String scenarioPattern, Integer page, Integer size) {
    var pageable = PageRequest.of(
      Optional.ofNullable(page).orElse(PAGEABLE_DEFAULT_PAGE),
      Optional.ofNullable(size).orElse(PAGEABLE_DEFAULT_SIZE)
    );
    Page<ScenarioEntity> scenarioEntities = scenarioRepository.findScenarioByLike(scenarioPattern + "%", pageable);
    LOGGER.info("scenario search list - {}", scenarioEntities.getTotalElements());
    List<Scenario> scenarios = ScenarioMapper.INSTANCE.toModel(scenarioEntities.getContent());

    return ScenarioPage.builder()
      .content(scenarios)
      .first(scenarioEntities.isFirst())
      .empty(scenarioEntities.isEmpty())
      .last(scenarioEntities.isLast())
      .page(scenarioEntities.getPageable().getPageNumber())
      .size(scenarioEntities.getPageable().getPageSize())
      .elements(scenarioEntities.getSize())
      .total(scenarioEntities.getTotalElements())
      .pages(scenarioEntities.getTotalPages())
      .build();
  }

  /**
   * @param scenarioId
   * @param key
   * @param file
   * @param modifiedBy
   * @return FileData
   */
  private FileData updateFile(UUID scenarioId, String key, MultipartFile file, String modifiedBy) {
    fileStorageService.deleteFile(key);
    return fileStorageService.uploadFile(scenarioId, file, modifiedBy);
  }


  /**
   * @param scenarioId
   * @return boolean
   */
  private boolean isScenarioAssociatedWithSimulation(UUID scenarioId) {
    List<SimulationEntity> simulationEntities = simulationRepository.findAll();
    List<SimulationEntity> matchedData = simulationEntities.stream().filter(sim -> sim.getScenarios().stream()
      .anyMatch(t -> t.equals(scenarioId))).collect(Collectors.toList());
    return matchedData.isEmpty();
  }

}
