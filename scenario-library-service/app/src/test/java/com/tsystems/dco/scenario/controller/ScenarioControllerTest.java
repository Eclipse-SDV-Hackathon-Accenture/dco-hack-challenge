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

package com.tsystems.dco.scenario.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tsystems.dco.App;
import com.tsystems.dco.model.ScenarioInput;
import com.tsystems.dco.scenario.service.ScenarioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ContextConfiguration(classes = App.class)
@AutoConfigureMockMvc
class ScenarioControllerTest {

  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private ObjectMapper objectMapper;
  @MockBean
  private ScenarioService scenarioService;
  private static final String TEST = "TEST";

  @Test
  void createScenario() throws Exception {
    ScenarioInput scenarioInput = ScenarioInput.builder().name(TEST).build();
    MockMultipartFile file = new MockMultipartFile(TEST, TEST, MediaType.APPLICATION_PDF_VALUE, TEST.getBytes(StandardCharsets.UTF_8));
    MockMultipartFile metadata = new MockMultipartFile(TEST, TEST, MediaType.APPLICATION_JSON_VALUE,
      objectMapper.writeValueAsString(scenarioInput).getBytes(StandardCharsets.UTF_8));
    mockMvc.perform(
        multipart("/api/scenario")
          .file(file)
          .file(metadata)
          .header("Authorization", getHeader())
          .accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isCreated());
    verify(scenarioService).createScenario(any(), any());
  }

  @Test
  void deleteScenarioById() throws Exception {
    mockMvc.perform(delete("/api/scenario?id=" + UUID.randomUUID())
        .contentType(MediaType.APPLICATION_JSON)
        .header("Authorization", getHeader()))
      .andDo(print())
      .andExpect(status().isNoContent()).andReturn();
    verify(scenarioService).deleteScenarioById(any());
  }

  @Test
  void scenarioReadByQuery() throws Exception {
    mockMvc.perform(get("/api/scenario")
        .param("page", "0")
        .param("size", "10")
        .header("Authorization", getHeader())
        .contentType(MediaType.APPLICATION_JSON))
      .andDo(print())
      .andExpect(status().isOk()).andReturn();
    verify(scenarioService).readScenarioByQuery(any(), any(), any(), any(), any());
  }

  @Test
  void searchScenarioByPattern() throws Exception {
    mockMvc.perform(get("/api/scenario/search")
        .param("scenarioPattern", "t")
        .param("page", "0")
        .param("size", "10")
        .header("Authorization", getHeader())
        .contentType(MediaType.APPLICATION_JSON))
      .andDo(print())
      .andExpect(status().isOk()).andReturn();
    verify(scenarioService).searchScenarioByPattern(anyString(), anyInt(), anyInt());
  }

  @Test
  void scenarioUpdateById() throws Exception {
    MockMultipartFile file = new MockMultipartFile(TEST, TEST,"text/plain", TEST.getBytes());
    MockMultipartHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart("/api/scenario");
    builder.with(new RequestPostProcessor() {
      @Override
      public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
        request.setMethod("PUT");
        return request;
      }
    });
    mockMvc.perform(builder
        .file(file)
        .param("id", String.valueOf(UUID.randomUUID()))
        .header("Authorization", getHeader()))
      .andExpect(status().isOk());
  }

  private String getHeader(){
    return "Basic "+ Base64.getEncoder().encodeToString("developer:password".getBytes());
  }
}
