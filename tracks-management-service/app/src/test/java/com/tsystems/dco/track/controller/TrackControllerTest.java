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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tsystems.dco.App;
import com.tsystems.dco.model.Track;
import com.tsystems.dco.model.TrackInput;
import com.tsystems.dco.model.TrackPage;
import com.tsystems.dco.track.service.TrackService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = App.class)
@AutoConfigureMockMvc
class TrackControllerTest {

  @Autowired
  private MockMvc mockMvc;
  @MockBean
  private TrackService trackService;
  private static final String TEST = "test";


  @Test
  void createTrack() throws Exception {
    given(trackService.createTrack(any())).willReturn(Track.builder().name(TEST).build());
    TrackInput trackInput = TrackInput.builder().name(TEST).build();
    ObjectMapper objectMapper = new ObjectMapper();
    mockMvc.perform(post("/api/track")
        .contentType(MediaType.APPLICATION_JSON)
        .header("Authorization", getHeader())
        .content(objectMapper.writeValueAsString(trackInput)))
      .andDo(print())
      .andExpect(status().isCreated()).andReturn();
    verify(trackService).createTrack(any());
  }

  @Test
  void deleteTrackById() throws Exception {
    doNothing().when(trackService).deleteTrackById(any());
    mockMvc.perform(delete("/api/track?id=" + UUID.randomUUID())
        .header("Authorization", getHeader())
        .contentType(MediaType.APPLICATION_JSON))
      .andDo(print())
      .andExpect(status().isNoContent());
    verify(trackService).deleteTrackById(any());
  }

  @Test
  void findTrackById() throws Exception {
    given(trackService.findTrackById(any())).willReturn(Track.builder().build());
    mockMvc.perform(get("/api/track/" + UUID.randomUUID())
        .header("Authorization", getHeader())
        .contentType(MediaType.APPLICATION_JSON))
      .andDo(print())
      .andExpect(status().isOk());
    verify(trackService).findTrackById(any());
  }

  @Test
  void findTrackByIds() throws Exception {
    List<Track> trackList = new ArrayList<>();
    trackList.add(Track.builder().build());
    given(trackService.findTrackByIds(any())).willReturn(trackList);
    mockMvc.perform(get("/api/track/list?trackIds=" + UUID.randomUUID())
        .header("Authorization", getHeader())
        .contentType(MediaType.APPLICATION_JSON))
      .andDo(print())
      .andExpect(status().isOk());
    verify(trackService).findTrackByIds(any());
  }

  @Test
  void isTracksExists() throws Exception {
    given(trackService.isTracksExists(any())).willReturn(true);
    mockMvc.perform(get("/api/track/validate?trackIds=" + UUID.randomUUID())
        .header("Authorization", getHeader())
        .contentType(MediaType.APPLICATION_JSON))
      .andDo(print())
      .andExpect(status().isOk());
    verify(trackService).isTracksExists(any());
  }

  @Test
  void searchTrackByPattern() throws Exception {
    given(trackService.searchTrackByPattern(anyString(), anyInt(), anyInt())).willReturn(new TrackPage());
    mockMvc.perform(get("/api/track/search?trackPattern=abc&page=0&size=10")
        .header("Authorization", getHeader())
        .contentType(MediaType.APPLICATION_JSON))
      .andDo(print())
      .andExpect(status().isOk());
    verify(trackService).searchTrackByPattern(anyString(), anyInt(), anyInt());
  }

  @Test
  void trackReadByQuery() throws Exception {
    given(trackService.readTrackByQuery(any(), any(), any(), any(), any())).willReturn(new TrackPage());
    mockMvc.perform(get("/api/track?page=0&size=10")
        .header("Authorization", getHeader())
        .contentType(MediaType.APPLICATION_JSON))
      .andDo(print())
      .andExpect(status().isOk());
    verify(trackService).readTrackByQuery(any(), any(), any(), any(), any());
  }

  @Test
  void getHardwareModule() throws Exception {
    given(trackService.getHardwareModule()).willReturn(new ArrayList<>());
    mockMvc.perform(get("/api/track/hardware")
        .header("Authorization", getHeader())
        .contentType(MediaType.APPLICATION_JSON))
      .andDo(print())
      .andExpect(status().isOk());
    verify(trackService).getHardwareModule();
  }

  private String getHeader(){
    return "Basic "+ Base64.getEncoder().encodeToString("developer:password".getBytes());
  }
}
