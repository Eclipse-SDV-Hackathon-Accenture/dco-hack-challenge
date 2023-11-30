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

package com.tsystems.dco.vehicle.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tsystems.dco.exception.DataNotFoundException;
import com.tsystems.dco.model.VehiclePage;
import com.tsystems.dco.model.VehicleResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VehicleServiceImpl implements VehicleService {

  private static final Logger LOGGER = LoggerFactory.getLogger(VehicleServiceImpl.class);

  private final ResourceLoader resourceLoader;


  /**
   * @param query
   * @param search
   * @param page
   * @param size
   * @param sort
   * @return VehiclePage
   */
  @Override
  public VehiclePage getVehicles(String query, String search, Integer page, Integer size, List<String> sort) {
    //get Vehicle metadata from Device Management Service
    return getMockVehicleData();
  }


  /**
   * @param vin
   * @return VehicleResponse
   */
  @Override
  public VehicleResponse getVehicleByVin(String vin) {
    VehiclePage vehiclePage = getMockVehicleData();
    List<VehicleResponse> vehicleResponses = vehiclePage.getContent();
    Optional<VehicleResponse> vehicleOptional = vehicleResponses.stream().filter(vehicle -> vehicle.getVin().equals(vin)).findAny();
    if (vehicleOptional.isPresent()) {
      return vehicleOptional.get();
    }
    throw new DataNotFoundException(HttpStatus.NOT_FOUND, "No Data available with this vin");
  }

  /**
   * @return VehiclePage
   */
  private VehiclePage getMockVehicleData() {
    var resource = resourceLoader.getResource("classpath:/vehicle-metadata.json");
    var mapper = new ObjectMapper();
    VehiclePage vehiclePage;
    try {
      var inputStream = resource.getInputStream();
      vehiclePage = mapper.readValue(inputStream, new TypeReference<>() {
      });
    } catch (IOException e) {
      LOGGER.error("Exception encountered while getting vehicle metadata");
      throw new DataNotFoundException(HttpStatus.INTERNAL_SERVER_ERROR, "Exception encountered while getting vehicle metadata");
    }
    return vehiclePage;
  }
}
