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

package com.tsystems.dco.track.feign;

import com.tsystems.dco.scenario.feign.FeignClientConfiguration;
import com.tsystems.dco.track.model.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@FeignClient(
  name = "track",
  url = "${app.track.rest.url}",
  configuration = FeignClientConfiguration.class
)
public interface TrackFeignClient {

  @PostMapping(value = "/api/track", produces = {"application/json"}, consumes = {"application/json"})
  ResponseEntity<Track> createTrack(@RequestBody TrackInput trackInput);

  @DeleteMapping(value = "/api/track", produces = {"application/json"})
  ResponseEntity<String> deleteTrackById(@RequestParam(value = "id") UUID id);

  @GetMapping(value = "/api/track/{id}", produces = {"application/json"})
  ResponseEntity<Track> findTrackById(@PathVariable("id") UUID id);

  @GetMapping(value = "/api/track/search", produces = {"application/json"})
  ResponseEntity<TrackPage> searchTrackByPattern(@RequestParam(value = "trackPattern") String trackPattern,
                                                 @RequestParam(value = "page") Integer page,
                                                 @RequestParam(value = "size") Integer size);

  @GetMapping(value = "/api/track", produces = {"application/json"})
  ResponseEntity<TrackPage> trackReadByQuery(@RequestParam(value = "query") String query,
                                             @RequestParam(value = "search") String search,
                                             @RequestParam(value = "page") Integer page,
                                             @RequestParam(value = "size") Integer size,
                                             @RequestParam(value = "sort") List<String> sort);

  @GetMapping(value = "/api/vehicle", produces = {"application/json"})
  ResponseEntity<VehiclePage> vehicleReadByQuery(@RequestParam(value = "query") String query,
                                                 @RequestParam(value = "search") String search,
                                                 @RequestParam(value = "page") Integer page,
                                                 @RequestParam(value = "size") Integer size,
                                                 @RequestParam(value = "sort") List<String> sort);

  @GetMapping(value = "/api/track/hardware", produces = {"application/json"})
  ResponseEntity<List<String>> getHardwareModule();

  @GetMapping(value = "/api/vehicle/{vin}", produces = { "application/json" })
  ResponseEntity<VehicleResponse> getVehicleByVin(@PathVariable("vin") String vin);

}
