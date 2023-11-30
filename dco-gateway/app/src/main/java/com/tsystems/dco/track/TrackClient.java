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

import com.tsystems.dco.track.model.*;
import java.util.List;
import java.util.UUID;

public interface TrackClient {

  Track createTrack(TrackInput trackInput);

  String deleteTrackById(UUID id);

  Track findTrackById(UUID id);

  TrackPage searchTrackByPattern(String trackPattern, Integer page, Integer size);

  TrackPage trackReadByQuery(String query, String search, Integer page, Integer size, List<String> sort);

  VehiclePage vehicleReadByQuery(String query, String search, Integer page, Integer size, List<String> sort);

  List<String> getHardwareModule();

  VehicleResponse getVehicleByVin(String vin);
}
