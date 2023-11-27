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

import com.tsystems.dco.track.feign.TrackFeignClient;
import com.tsystems.dco.track.model.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class TrackRestClient implements TrackClient {

  private static final Logger LOGGER = LoggerFactory.getLogger(TrackRestClient.class);

  private final TrackFeignClient client;

  /**
   * @param trackInput
   * @return Track
   */
  @Override
  public Track createTrack(TrackInput trackInput) {
    return client.createTrack(trackInput).getBody();
  }

  /**
   * @param id
   * @return String
   */
  @Override
  public String deleteTrackById(UUID id) {
    return client.deleteTrackById(id).getBody();
  }


  /**
   * @param id
   * @return Track
   */
  @Override
  public Track findTrackById(UUID id) {
    return client.findTrackById(id).getBody();
  }

  /**
   * @param trackPattern
   * @param page
   * @param size
   * @return TrackPage
   */
  @Override
  public TrackPage searchTrackByPattern(String trackPattern, Integer page, Integer size) {
    return client.searchTrackByPattern(trackPattern, page, size).getBody();
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
  public TrackPage trackReadByQuery(String query, String search, Integer page, Integer size, List<String> sort) {
    return client.trackReadByQuery(query, search, page, size, sort).getBody();
  }

  /**
   * @param query
   * @param search
   * @param page
   * @param size
   * @param sort
   * @return VehiclePage
   */
  @Override
  public VehiclePage vehicleReadByQuery(String query, String search, Integer page, Integer size, List<String> sort) {
    return client.vehicleReadByQuery(query, search, page, size, sort).getBody();
  }

  /**
   * @return List
   */
  @Override
  public List<String> getHardwareModule() {
    return client.getHardwareModule().getBody();
  }

  /**
   * @param vin
   * @return VehicleResponse
   */
  @Override
  public VehicleResponse getVehicleByVin(String vin) {
    return client.getVehicleByVin(vin).getBody();
  }

}
