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

package com.tsystems.dco.track;

import com.tsystems.dco.track.model.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class TrackController {

  private static final Logger LOGGER = LoggerFactory.getLogger(TrackController.class);

  private final TrackClient trackClient;

  /**
   * @param trackInput
   * @return Track
   */
  @MutationMapping
  public Track createTrack(@Argument TrackInput trackInput) {
    LOGGER.info("TrackInput  : {}", trackInput);
    return trackClient.createTrack(trackInput);
  }

  /**
   * @param query
   * @param search
   * @param page
   * @param size
   * @param sort
   * @return TrackPage
   */
  @QueryMapping
  public TrackPage trackReadByQuery(@Argument String query, @Argument String search, @Argument Integer page, @Argument Integer size, @Argument List<String> sort) {
    LOGGER.info("Track read by query");
    return trackClient.trackReadByQuery(query, search, page, size, sort);
  }


  /**
   * @param id
   * @return String
   */
  @MutationMapping
  public String deleteTrackById(@Argument UUID id) {
    LOGGER.info("Deleting Track by id : {}", id);
    trackClient.deleteTrackById(id);
    return "Track deleted - " + id;
  }

  /**
   * @param id
   * @return Track
   */
  @QueryMapping
  public Track findTrackById(@Argument UUID id) {
    LOGGER.info("Track by id  : {}", id);
    return trackClient.findTrackById(id);
  }


  /**
   * @param trackPattern
   * @param page
   * @param size
   * @return TrackPage
   */
  @QueryMapping
  public TrackPage searchTrackByPattern(@Argument String trackPattern, @Argument Integer page, @Argument Integer size) {
    LOGGER.info("Track search by Pattern : {}", trackPattern);
    return trackClient.searchTrackByPattern(trackPattern, page, size);
  }


  /**
   * @param query
   * @param search
   * @param page
   * @param size
   * @param sort
   * @return VehiclePage
   */
  @QueryMapping
  public VehiclePage vehicleReadByQuery(@Argument String query, @Argument String search, @Argument Integer page, @Argument Integer size, @Argument List<String> sort) {
    LOGGER.info("Track read by query");
    return trackClient.vehicleReadByQuery(query, search, page, size, sort);
  }

  /**
   * @return List
   */
  @QueryMapping
  public List<String> getHardwareModule() {
    LOGGER.info("read hardware module");
    return trackClient.getHardwareModule();
  }

  /**
   * @param vin
   * @return VehicleResponse
   */
  @QueryMapping
  public VehicleResponse getVehicleByVin(@Argument String vin) {
    return trackClient.getVehicleByVin(vin);
  }

}
