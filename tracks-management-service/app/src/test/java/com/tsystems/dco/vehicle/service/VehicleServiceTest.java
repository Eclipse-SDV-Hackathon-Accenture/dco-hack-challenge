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

import com.tsystems.dco.exception.DataNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class VehicleServiceTest {

  @InjectMocks
  private VehicleServiceImpl vehicleService;
  @Mock
  private ResourceLoader resourceLoader;
  @Mock
  private Resource resource;

  @Test
  void getVehicles() throws IOException {
    getVehicleData();
    assertNotNull(vehicleService.getVehicles(null, null, 0, 10, null));
  }

  @Test
  void getVehiclesWithError() throws IOException {
    given(resourceLoader.getResource(any())).willReturn(resource);
    given(resource.getInputStream()).willThrow(IOException.class);
    assertThrows(DataNotFoundException.class, () -> vehicleService.getVehicles(null, null, 0, 10, null));
  }

  @Test
  void getVehicleByVin() throws IOException {
    getVehicleData();
    assertNotNull(vehicleService.getVehicleByVin("BBTEST00000000340"));
  }

  @Test
  void getVehicleByVinWithError() throws IOException {
    getVehicleData();
    assertThrows(DataNotFoundException.class, () -> vehicleService.getVehicleByVin("test"));
  }

  private void getVehicleData() throws IOException {
    InputStream inputStream = new FileInputStream("src/test/resources/vehicle-test.json");
    given(resourceLoader.getResource(any())).willReturn(resource);
    given(resource.getInputStream()).willReturn(inputStream);
  }
}
