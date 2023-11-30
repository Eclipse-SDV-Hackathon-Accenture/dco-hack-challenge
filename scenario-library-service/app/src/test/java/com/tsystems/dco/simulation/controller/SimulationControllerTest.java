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

package com.tsystems.dco.simulation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tsystems.dco.App;
import com.tsystems.dco.model.SimulationInput;
import com.tsystems.dco.model.SimulationPage;
import com.tsystems.dco.simulation.service.SimulationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Base64;
import java.util.UUID;

import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = App.class)
@AutoConfigureMockMvc
class SimulationControllerTest {

  @Autowired
  private MockMvc mockMvc;
  @MockBean
  private SimulationService simulationService;
  private static final String TEST = "test";

  @Test
  void launchSimulation() throws Exception {
    given(simulationService.launchSimulation(any())).willReturn(TEST);
    SimulationInput simulationInput = SimulationInput.builder().platform(TEST).build();
    ObjectMapper objectMapper = new ObjectMapper();
    mockMvc.perform(post("/api/simulation")
        .contentType(MediaType.APPLICATION_JSON)
        .header("Authorization", getHeader())
        .content(objectMapper.writeValueAsString(simulationInput)))
      .andDo(print())
      .andExpect(status().isCreated()).andReturn();
    verify(simulationService).launchSimulation(any());
  }

  @Test
  void simulationReadByQuery() throws Exception {
    given(simulationService.simulationReadByQuery(any(), any(), any(), any(), any())).willReturn(new SimulationPage());
    mockMvc.perform(get("/api/simulation")
        .param("page", "10")
        .header("Authorization", getHeader())
        .contentType(MediaType.APPLICATION_JSON))
      .andDo(print())
      .andExpect(status().isOk()).andReturn().getResponse();
    verify(simulationService).simulationReadByQuery(any(), any(), any(), any(), any());
  }

  @Test
  void isTrackAssociatedWithSimulation() throws Exception {
    given(simulationService.isTrackAssociatedWithSimulation(any())).willReturn(true);
    mockMvc.perform(get("/api/simulation/track?id=" + UUID.randomUUID())
        .header("Authorization", getHeader())
        .contentType(MediaType.APPLICATION_JSON))
      .andDo(print())
      .andExpect(status().isOk()).andReturn().getResponse();
    verify(simulationService).isTrackAssociatedWithSimulation(any());
  }

  private String getHeader(){
    return "Basic "+ Base64.getEncoder().encodeToString("developer:password".getBytes());
  }
}
