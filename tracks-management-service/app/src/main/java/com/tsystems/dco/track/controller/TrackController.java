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

package com.tsystems.dco.track.controller;

import com.tsystems.dco.model.Track;
import com.tsystems.dco.model.TrackInput;
import com.tsystems.dco.model.TrackPage;
import com.tsystems.dco.track.service.TrackService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import com.tsystems.dco.api.TrackApi;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class TrackController implements TrackApi {

  private static final Logger LOGGER = LoggerFactory.getLogger(TrackController.class);

  private final TrackService trackService;

  /**
   * POST /api/track : Create a Track
   * Create a Track to database
   *
   * @param trackInput (required)
   * @return OK (status code 200)
   * or Bad Request (status code 400)
   * or Not Found (status code 404)
   */
  @Override
  public ResponseEntity<Track> createTrack(TrackInput trackInput) {
    LOGGER.info("creating track : {}", trackInput.getName());
    return ResponseEntity
      .status(HttpStatus.CREATED)
      .body(trackService.createTrack(trackInput));
  }

  /**
   * DELETE /api/track : Delete a track by id
   * Delete a track by id from database
   *
   * @param id The track id (required)
   * @return OK (status code 200)
   * or Bad Request (status code 400)
   * or Not Found (status code 404)
   */
  @Override
  public ResponseEntity<String> deleteTrackById(UUID id) {
    LOGGER.info("Deleting track : {}", id);
    trackService.deleteTrackById(id);
    return ResponseEntity
      .status(HttpStatus.NO_CONTENT)
      .build();
  }

  /**
   * GET /api/track/{id} : Find track by id
   * Find track by id
   *
   * @param id The track id (required)
   * @return OK (status code 200)
   * or Not Found (status code 404)
   */
  @Override
  public ResponseEntity<Track> findTrackById(UUID id) {
    LOGGER.info("find  track by id : {}", id);
    return ResponseEntity
      .status(HttpStatus.OK)
      .body(trackService.findTrackById(id));
  }

  /**
   * GET /api/track/ids : Find track by ids
   * Find track by ids
   *
   * @param trackIds The track ids (required)
   * @return OK (status code 200)
   * or Not Found (status code 404)
   */
  @Override
  public ResponseEntity<List<Track>> findTrackByIds(List<UUID> trackIds) {
    LOGGER.info("find Tracks By Ids list : {}", trackIds);
    return ResponseEntity
      .status(HttpStatus.OK)
      .body(trackService.findTrackByIds(trackIds));
  }

  /**
   * GET /api/track/hardware : Read Hardware Module
   * Read Hardware Module
   *
   * @return OK (status code 200)
   * or Not Found (status code 404)
   */
  @Override
  public ResponseEntity<List<String>> getHardwareModule() {
    LOGGER.info("get hardware module list");
    return ResponseEntity
      .status(HttpStatus.OK)
      .body(trackService.getHardwareModule());
  }

  /**
   * GET /api/track/validate : Check track exists
   * Check track exists
   *
   * @param trackIds The track ids (required)
   * @return OK (status code 200)
   * or Not Found (status code 404)
   */
  @Override
  public ResponseEntity<Boolean> isTracksExists(List<UUID> trackIds) {
    return ResponseEntity
      .status(HttpStatus.OK)
      .body(trackService.isTracksExists(trackIds));
  }

  /**
   * GET /api/track/search : Search for tracks with given &#39;%&#39; pattern. Returns paginated list
   * Search for tracks with given &#39;%&#39; pattern. Returns paginated list
   *
   * @param trackPattern (required)
   * @param page         (optional)
   * @param size         (optional)
   * @return OK (status code 200)
   * or Not Found (status code 404)
   */
  @Override
  public ResponseEntity<TrackPage> searchTrackByPattern(String trackPattern, Integer page, Integer size) {
    LOGGER.info("searching tracks by pattern : {}", trackPattern);
    return ResponseEntity
      .status(HttpStatus.OK)
      .body(trackService.searchTrackByPattern(trackPattern, page, size));
  }

  /**
   * GET /api/track : Read Track by query
   * Read Track by query and pageable from database
   *
   * @param query  Comma separated list of &#x60;{field}{operation}{value}&#x60; where operation can be  &#x60;:&#x60; for equal,  &#x60;!&#x60; for not equal and  &#x60;~&#x60; for like operation (optional)
   * @param search Search value to query searchable fields agains (optional)
   * @param page   (optional)
   * @param size   (optional)
   * @param sort   (optional)
   * @return OK (status code 200)
   * or Not Found (status code 404)
   */
  @Override
  public ResponseEntity<TrackPage> trackReadByQuery(String query, String search, Integer page, Integer size, List<String> sort) {
    LOGGER.info("fetching list of tracks with size : {}", size);
    return ResponseEntity
      .status(HttpStatus.OK)
      .body(trackService.readTrackByQuery(query, search, page, size, sort));
  }
}
