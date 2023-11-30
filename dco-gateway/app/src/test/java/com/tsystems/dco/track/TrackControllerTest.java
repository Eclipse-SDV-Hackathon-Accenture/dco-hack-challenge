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

import com.tsystems.dco.exception.BaseException;
import com.tsystems.dco.track.model.Track;
import com.tsystems.dco.track.model.TrackPage;
import com.tsystems.dco.track.model.VehiclePage;
import com.tsystems.dco.track.model.VehicleResponse;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.anyString;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureGraphQlTester;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureGraphQlTester
class TrackControllerTest {

  @Autowired
  private GraphQlTester tester;

  @MockBean
  private TrackRestClient client;

  @Test
  void createTrack() {
    var track = """
      mutation CREATE_TRACK {
        createTrack (
          trackInput: {
            name: "Test Track"
            trackType: "Test"
            duration: "7"
            state: "CREATED"
            description: "Test Desc"
            vehicles: [
              {
                vin: "VINTEST1HQMIOUT08"
                country: "DE"
              }
            ]
          }
        ){
          id
        }
      }
      """;
    when(client.createTrack(any())).thenReturn(new Track());
    tester.document(track).execute().errors().satisfy(error -> assertThat(error.size() == 0));
  }


  @Test
  void trackReadByQuery() {
    var tracks = """
      query LIST_TRACK {
        trackReadByQuery(search: null, query: null, page: 0, size: 2, sort: null){
         content{
           id
         	name
         	state
         	trackType
         }
         total
        }
       }    
      """;
    when(client.trackReadByQuery(any(), any(), anyInt(), anyInt(), any())).thenReturn(new TrackPage());
    tester.document(tracks).execute().errors().satisfy(error -> assertThat(error.size() == 0));
  }

  @Test
  void deleteTrackById() {
    var track = """
      mutation DELETE_TRACK {
              deleteTrackById(id : "e8e996c5-8081-43c9-9d97-cb170eb0eee5")
            }
      """;
    when(client.deleteTrackById(any())).thenReturn("test");
    tester.document(track).execute().errors().satisfy(error -> assertThat(error.size() == 0));
  }

  @Test
  void findTrackById() {
    var track = """
      query TRACK_BY_ID {
               findTrackById(id : "818094c5-0be4-4a0d-bf9f-0c70919d05ee") {
                 id
                 name
                 vehicles{
                   vin
                   devices {
                     id
                     type
                     status
                   }
                 }
               }
             }
      """;
    when(client.findTrackById(any())).thenReturn(new Track());
    tester.document(track).execute().errors().satisfy(error -> assertThat(error.size() == 0));
  }

  @Test
  void searchTrackByPattern() {
    var tracks = """
      query SEARCH_TRACK {
      	searchTrackByPattern(trackPattern: "eu", page:0, size:11){
          content{
            id
            name
          }
          total
        }
      }
            
      """;
    when(client.searchTrackByPattern(anyString(), anyInt(), anyInt())).thenReturn(new TrackPage());
    tester.document(tracks).execute().errors().satisfy(error -> assertThat(error.size() == 0));
  }

  @Test
  void vehicleReadByQuery() {
    var vehicles = """
      query LIST_VEHICLE {
             vehicleReadByQuery(search: null, query: null, page: 0, size: 2, sort: null){
              content{
                vin
                status
                type
                fleets {
                  id
                  name
                  type
                }
             }
             total
             }
           }
            
      """;
    when(client.vehicleReadByQuery(any(), any(), anyInt(), anyInt(), any())).thenReturn(new VehiclePage());
    tester.document(vehicles).execute().errors().satisfy(error -> assertThat(error.size() == 0));
  }

  @Test
  void findTrackByIdWithError() {
    var track = """
      query TRACK_BY_ID {
               findTrackById(id : "818094c5-0be4-4a0d-bf9f-0c70919d05ee") {
                 id
                 name
                 vehicles{
                   vin
                   devices {
                     id
                     type
                     status
                   }
                 }
               }
             }
      """;
    when(client.findTrackById(any())).thenThrow(new BaseException(HttpStatus.NOT_FOUND, "[{\"message\":\"Track with id 818094c5-0be4-4a0d-bf9f-0c70919d05ee not found.\",\"status\":\"NOT_FOUND\"}]"));
    tester.document(track).execute().errors().satisfy(error -> assertThat(error.size() == 0));
  }

  @Test
  void getHardwareModule() {
    var hardware = """
      query GET_HARDWARE_MODULE {
         getHardwareModule
       }    
      """;
    when(client.getHardwareModule()).thenReturn(new ArrayList<>());
    tester.document(hardware).execute().errors().satisfy(error -> assertThat(error.size() == 0));
  }

  @Test
  void getVehicleByVin() {
    var vehicle = """
      query VEHICLE_BY_VIN{
        getVehicleByVin(vin: "BBTEST00000000340"){
          vin
          model
          brand
        }
      }  
      """;
    when(client.getVehicleByVin(anyString())).thenReturn(new VehicleResponse());
    tester.document(vehicle).execute().errors().satisfy(error -> assertThat(error.size() == 0));
  }
}
