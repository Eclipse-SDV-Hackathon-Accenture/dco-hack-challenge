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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.verify;

@ContextConfiguration(classes = {TrackRestClient.class})
@ExtendWith(SpringExtension.class)
class TrackRestClientTest {

  @Autowired
  private TrackRestClient trackRestClient;
  @MockBean
  private TrackFeignClient client;
  @Mock
  private ResponseEntity<TrackPage> trackPageResponseEntity;
  @Mock
  private ResponseEntity<String> responseEntity;
  @Mock
  private ResponseEntity<Track> trackResponseEntity;
  @Mock
  private ResponseEntity<VehiclePage> vehiclePageResponseEntity;
  @Mock
  private ResponseEntity<List<String>> listResponseEntity;
  @Mock
  private ResponseEntity<VehicleResponse> vehicleResponseResponseEntity;

  private final static String TEST = "Test";

  @Test
  void createTrack() {
    Track track = new Track();
    given(trackResponseEntity.getBody()).willReturn(track);
    given(client.createTrack(any())).willReturn(trackResponseEntity);
    assertSame(track, trackRestClient.createTrack(new TrackInput()));
    verify(client).createTrack(any());
    verify(trackResponseEntity).getBody();
  }


  @Test
  void deleteTrackById() {
    given(responseEntity.getBody()).willReturn(TEST);
    given(client.deleteTrackById(any())).willReturn(responseEntity);
    assertSame(TEST, trackRestClient.deleteTrackById(UUID.randomUUID()));
    verify(client).deleteTrackById(any());
    verify(responseEntity).getBody();
  }

  @Test
  void findTrackById() {
    Track track = new Track();
    given(trackResponseEntity.getBody()).willReturn(track);
    given(client.findTrackById(any())).willReturn(trackResponseEntity);
    assertSame(track, trackRestClient.findTrackById(UUID.randomUUID()));
    verify(client).findTrackById(any());
    verify(trackResponseEntity).getBody();

  }

  @Test
  void searchTrackByPattern() {
    TrackPage trackPage = new TrackPage();
    given(trackPageResponseEntity.getBody()).willReturn(trackPage);
    given(client.searchTrackByPattern(anyString(), anyInt(), anyInt())).willReturn(trackPageResponseEntity);
    assertSame(trackPage, trackRestClient.searchTrackByPattern(TEST, 0, 10));
    verify(client).searchTrackByPattern(anyString(), anyInt(), anyInt());
    verify(trackPageResponseEntity).getBody();
  }

  @Test
  void trackReadByQuery() {
    TrackPage trackPage = new TrackPage();
    given(trackPageResponseEntity.getBody()).willReturn(trackPage);
    given(client.trackReadByQuery(any(), any(), any(), any(), any())).willReturn(trackPageResponseEntity);
    trackRestClient.trackReadByQuery(null, null, 0, 10, null);
    verify(client).trackReadByQuery(any(), any(), any(), any(), any());
    verify(trackPageResponseEntity).getBody();
  }

  @Test
  void vehicleReadByQuery() {
    VehiclePage vehiclePage = new VehiclePage();
    given(vehiclePageResponseEntity.getBody()).willReturn(vehiclePage);
    given(client.vehicleReadByQuery(any(), any(), any(), any(), any())).willReturn(vehiclePageResponseEntity);
    assertSame(vehiclePage, trackRestClient.vehicleReadByQuery(null, null, 0, 10, null));
    verify(client).vehicleReadByQuery(any(), any(), any(), any(), any());
    verify(vehiclePageResponseEntity).getBody();
  }

  @Test
  void getHardwareModule() {
    List<String> list = new ArrayList<>();
    list.add(TEST);
    given(listResponseEntity.getBody()).willReturn(list);
    given(client.getHardwareModule()).willReturn(listResponseEntity);
    assertSame(list, trackRestClient.getHardwareModule());
    verify(client).getHardwareModule();
    verify(listResponseEntity).getBody();
  }

  @Test
  void getVehicleByVin() {
    VehicleResponse vehicleResponse = new VehicleResponse();
    given(vehicleResponseResponseEntity.getBody()).willReturn(vehicleResponse);
    given(client.getVehicleByVin(anyString())).willReturn(vehicleResponseResponseEntity);
    assertSame(vehicleResponse, trackRestClient.getVehicleByVin(TEST));
    verify(client).getVehicleByVin(anyString());
    verify(vehicleResponseResponseEntity).getBody();
  }
}
