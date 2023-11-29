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

package com.tsystems.dco.vehicle.controller;

import com.tsystems.dco.model.VehiclePage;
import com.tsystems.dco.model.VehicleResponse;
import com.tsystems.dco.vehicle.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import com.tsystems.dco.api.VehicleApi;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class VehicleController implements VehicleApi {

  private static final Logger LOGGER = LoggerFactory.getLogger(VehicleController.class);

  private final VehicleService vehicleService;

  /**
   * GET /api/vehicle/{vin} : Find vehicle by vin
   * Find vehicle by vin
   *
   * @param vin The vehicle vin (required)
   * @return OK (status code 200)
   * or Not Found (status code 404)
   */
  @Override
  public ResponseEntity<VehicleResponse> getVehicleByVin(String vin) {
    LOGGER.info("get vehicle by vin {}", vin);
    return ResponseEntity
      .status(HttpStatus.OK)
      .body(vehicleService.getVehicleByVin(vin));
  }

  /**
   * GET /api/vehicle : Read vehicle by query
   * Read Vehicle by query and pageable from device service
   *
   * @param query  Comma separated list of &#x60;{field}{operation}{value}&#x60; where operation can be  &#x60;:&#x60; for equal,  &#x60;!&#x60; for not equal and  &#x60;~&#x60; for like operation (optional)
   * @param search Search value to query searchable fields against (optional)
   * @param page   (optional)
   * @param size   (optional)
   * @param sort   (optional)
   * @return OK (status code 200)
   * or Not Found (status code 404)
   */
  @Override
  public ResponseEntity<VehiclePage> vehicleReadByQuery(String query, String search, Integer page, Integer size, List<String> sort) {
    LOGGER.info("fetching list of vehicles");
    return ResponseEntity
      .status(HttpStatus.OK)
      .body(vehicleService.getVehicles(query, search, page, size, sort));
  }
}
