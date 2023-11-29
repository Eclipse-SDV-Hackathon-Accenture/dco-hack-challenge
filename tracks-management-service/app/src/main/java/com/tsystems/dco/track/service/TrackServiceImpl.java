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
import com.tsystems.dco.mapper.TrackMapper;
import com.tsystems.dco.model.*;
import com.tsystems.dco.track.entity.TrackEntity;
import com.tsystems.dco.track.repository.TrackRepository;
import com.tsystems.dco.vehicle.entity.VehicleEntity;
import com.tsystems.dco.vehicle.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class TrackServiceImpl implements TrackService {

  private static final Logger LOGGER = LoggerFactory.getLogger(TrackServiceImpl.class);

  private static final Sort.Order ORDER_DEFAULT = Sort.Order.asc("name");
  private static final Integer PAGEABLE_DEFAULT_PAGE = 0;
  private static final Integer PAGEABLE_DEFAULT_SIZE = 15;
  private static final String ERROR_NOT_FOUND_ID = "Track with id %s not found.";

  private final TrackRepository trackRepository;
  private final VehicleService vehicleService;
  private final ScenarioApiClient scenarioApiClient;


  /**
   * @param trackInput
   * @return Track
   */
  @Override
  public Track createTrack(TrackInput trackInput) {
    var trackEntity = TrackMapper.INSTANCE.toEntity(trackInput);
    LOGGER.info("saving track {}", trackInput.getName());
    return TrackMapper.INSTANCE.toModel(trackRepository.save(trackEntity));
  }


  /**
   * @param id
   */
  @Override
  public void deleteTrackById(UUID id) {
    final var deleted = trackRepository.findById(id)
      .orElseThrow(() -> new DataNotFoundException(HttpStatus.NOT_FOUND, String.format(ERROR_NOT_FOUND_ID, id)));
    LOGGER.info("Deleting track {}", id);
    boolean isTrackAssociatedWithSimulation = scenarioApiClient.isTrackAssociatedWithSimulation(deleted.getId()).getBody();
    if (!isTrackAssociatedWithSimulation) {
      LOGGER.info("Deleting track : {}", deleted.getId());
      trackRepository.delete(deleted);
    } else {
      LOGGER.error("Track id : {}, can't be deleted as it has an association with simulation", deleted.getId());
      throw new DataDeletionException(HttpStatus.BAD_REQUEST, "Track can't be deleted as it has an association with simulation");
    }
  }

  /**
   * @param query
   * @param search
   * @param page
   * @param size
   * @param sort
   * @return TrackPage
   */
  @Override
  public TrackPage readTrackByQuery(String query, String search, Integer page, Integer size, List<String> sort) {
    var orders = Optional.ofNullable(sort)
      .map(s -> s.stream()
        .map(so -> so.split(":"))
        .map(sp -> sp.length >= 2 ? new Sort.Order(Sort.Direction.fromString(sp[1]), sp[0]) : null)
        .filter(Objects::nonNull)
      ).orElse(Stream.of(ORDER_DEFAULT))
      .toList();

    var pageable = PageRequest.of(
      Optional.ofNullable(page).orElse(PAGEABLE_DEFAULT_PAGE),
      Optional.ofNullable(size).orElse(PAGEABLE_DEFAULT_SIZE),
      Sort.by(orders)
    );

    var specs = Optional.ofNullable(search)
      .map(s -> Specification
        .where(TrackQuery.Query.builder()
          .field("name").operation(TrackQuery.Operation.LIKE).value(s)
          .build())
        .or(TrackQuery.Query.builder()
          .field("trackType").operation(TrackQuery.Operation.LIKE).value(s)
          .build())
        .or(TrackQuery.Query.builder()
          .field("state").operation(TrackQuery.Operation.LIKE).value(s)
          .build()))
      .orElse(new TrackQuery(query).toSpecification());

    var queried = trackRepository.findAll(specs, pageable);
    return new TrackPage()
      .empty(queried.isEmpty())
      .first(queried.isFirst())
      .last(queried.isLast())
      .page(queried.getNumber())
      .size(queried.getSize())
      .pages(queried.getTotalPages())
      .elements(queried.getNumberOfElements())
      .total(queried.getTotalElements())
      .content(TrackMapper.INSTANCE.toModel(queried.getContent()));
  }


  /**
   * @param id
   * @return Track
   */
  @Override
  public Track findTrackById(UUID id) {
    var trackEntity = trackRepository.findById(id)
      .orElseThrow(() -> new DataNotFoundException(HttpStatus.NOT_FOUND, String.format(ERROR_NOT_FOUND_ID, id)));
    List<VehicleEntity> vehicleEntities = trackEntity.getVehicles();
    List<VehicleResponse> vehicleResponses = new ArrayList<>();
    vehicleEntities.forEach(entity -> vehicleResponses.add(vehicleService.getVehicleByVin(entity.getVin())));
    return TrackMapper.INSTANCE.toModel(trackEntity).vehicles(vehicleResponses);
  }


  /**
   * @param trackPattern
   * @param page
   * @param size
   * @return TrackPage
   */
  @Override
  public TrackPage searchTrackByPattern(String trackPattern, Integer page, Integer size) {
    var pageable = PageRequest.of(
      Optional.ofNullable(page).orElse(PAGEABLE_DEFAULT_PAGE),
      Optional.ofNullable(size).orElse(PAGEABLE_DEFAULT_SIZE)
    );
    Page<TrackEntity> trackPageEntity = trackRepository.findByTrackLike(trackPattern + "%", pageable);
    LOGGER.info("track search list - {}", trackPageEntity.getTotalElements());
    List<Track> tracks = TrackMapper.INSTANCE.toModel(trackPageEntity.getContent());
    return TrackPage.builder()
      .content(tracks)
      .first(trackPageEntity.isFirst())
      .empty(trackPageEntity.isEmpty())
      .last(trackPageEntity.isLast())
      .page(trackPageEntity.getPageable().getPageNumber())
      .size(trackPageEntity.getPageable().getPageSize())
      .elements(trackPageEntity.getSize())
      .total(trackPageEntity.getTotalElements())
      .pages(trackPageEntity.getTotalPages())
      .build();
  }


  /**
   * @param trackIds
   * @return List
   */
  @Override
  public List<Track> findTrackByIds(List<UUID> trackIds) {
    List<TrackEntity> tracksList = trackRepository.findTrackByIds(trackIds);
    LOGGER.info("track list size - {}", tracksList.size());
    List<Track> tracks = new ArrayList<>();
    tracksList.forEach(track -> {
      List<VehicleEntity> vehicleEntities = track.getVehicles();
      List<VehicleResponse> vehicleResponses = new ArrayList<>();
      vehicleEntities.forEach(entity -> vehicleResponses.add(vehicleService.getVehicleByVin(entity.getVin())));
      tracks.add(TrackMapper.INSTANCE.toModel(track).vehicles(vehicleResponses));
    });
    return tracks;
  }


  /**
   * @param trackIds
   * @return boolean
   */
  @Override
  public boolean isTracksExists(List<UUID> trackIds) {
    trackIds.forEach(track -> {
      if (!trackRepository.existsById(track))
        throw new DataNotFoundException(HttpStatus.BAD_REQUEST, "Track doesn't not exist");
    });
    return true;
  }

  /**
   * @return List
   */
  @Override
  public List<String> getHardwareModule() {
    return Arrays.asList("HARDWARE-OTA-998-1ZW6CQN0",
      "HARDWARE-OTA-998-1YYHZQOQ", "HARDWARE-OTA-998-1Y8B1UP3", "HARDWARE-OTA-998-1Y1DRJLH",
      "HARDWARE-OTA-998-1XTTN0QT", "HARDWARE-OTA-998-1XD761GW", "HARDWARE-OTA-998-1VU9GNY6",
      "HARDWARE-OTA-998-1VIE6J4F", "HARDWARE-OTA-998-1VF3LE6K", "HARDWARE-OTA-998-1VDOU4HA",
      "HARDWARE-OTA-998-1V4YXLLB", "HARDWARE-OTA-998-1UQRYVAI", "HARDWARE-OTA-998-1UJ1PW1A",
      "HARDWARE-OTA-998-1SAVET2I", "HARDWARE-OTA-998-1S0ZL2IQ", "HARDWARE-OTA-998-1RJ27SNK",
      "HARDWARE-OTA-998-1R5VFR0F", "HARDWARE-OTA-998-1QWJMAFN", "HARDWARE-OTA-998-1QHXLH9X",
      "HARDWARE-OTA-998-1QCR2YQZ", "HARDWARE-OTA-998-1Q5O1JY7", "HARDWARE-OTA-998-1PZG3A7R",
      "HARDWARE-OTA-998-1OQINXV0", "HARDWARE-OTA-998-1O660XQT", "HARDWARE-OTA-998-1O0MFN87",
      "HARDWARE-OTA-998-1MTXD5RI", "HARDWARE-OTA-998-1MILV08B", "HARDWARE-OTA-998-1MAL5I50",
      "HARDWARE-OTA-998-1LYTG36V", "HARDWARE-OTA-998-1LQJ5NI2", "HARDWARE-OTA-998-1L72BO0C",
      "HARDWARE-OTA-998-1L5L9N7G", "HARDWARE-OTA-998-1KT2KU3P", "HARDWARE-OTA-998-1K1XTY9R",
      "HARDWARE-OTA-998-1K1S2U46", "HARDWARE-OTA-998-1JMZJ77L", "HARDWARE-OTA-998-1JIMOONV",
      "HARDWARE-OTA-998-1JCHU54D", "HARDWARE-OTA-998-1JANYCHT", "HARDWARE-OTA-998-1J0FNH0Z");
  }
}
