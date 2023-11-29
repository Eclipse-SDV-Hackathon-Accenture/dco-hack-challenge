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

import com.tsystems.dco.App;
import com.tsystems.dco.model.VehiclePage;
import com.tsystems.dco.model.VehicleResponse;
import com.tsystems.dco.vehicle.service.VehicleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Base64;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = App.class)
@AutoConfigureMockMvc
class VehicleControllerTest {

  @Autowired
  private MockMvc mockMvc;
  @MockBean
  private VehicleService vehicleService;

  @Test
  void vehicleReadByQuery() throws Exception {
    given(vehicleService.getVehicles(any(), any(), any(), any(), any())).willReturn(new VehiclePage());
    mockMvc.perform(get("/api/vehicle?page=0&size=10")
        .header("Authorization", getHeader())
        .contentType(MediaType.APPLICATION_JSON))
      .andDo(print())
      .andExpect(status().isOk());
    verify(vehicleService).getVehicles(any(), any(), any(), any(), any());
  }

  @Test
  void getVehicleByVin() throws Exception {
    given(vehicleService.getVehicleByVin(any())).willReturn(new VehicleResponse());
    mockMvc.perform(get("/api/vehicle/VINTEST")
        .header("Authorization", getHeader())
        .contentType(MediaType.APPLICATION_JSON))
      .andDo(print())
      .andExpect(status().isOk());
    verify(vehicleService).getVehicleByVin(any());
  }

  private String getHeader(){
    return "Basic "+ Base64.getEncoder().encodeToString("developer:password".getBytes());
  }
}
