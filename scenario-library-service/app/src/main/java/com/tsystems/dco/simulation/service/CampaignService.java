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

package com.tsystems.dco.simulation.service;

import com.tsystems.dco.integration.Campaign;
import com.tsystems.dco.integration.CampaignRequest;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class CampaignService {

  private static final Logger LOGGER = LoggerFactory.getLogger(CampaignService.class);

  /**
   * @param campaignRequest
   * @return Campaign
   */
  public Campaign startCampaign(CampaignRequest campaignRequest) {
    LOGGER.info("campaign Request {}", campaignRequest);
    //calling  campaign service and get the campaign id as response
    //returning mocked campaign id

    return Campaign.builder().id(UUID.randomUUID()).status("Running").build();
  }

  /**
   * @param campaignId
   * @return Campaign
   */
  public Campaign checkStatus(UUID campaignId) {
    //calling  campaign service and get the campaign status as response
    // returning mocked campaign response
    return Campaign.builder()
      .id(campaignId)
      .status(getMockedCampaignStatus())
      .build();
  }

  /**
   * @return String
   */
  @SneakyThrows
  private String getMockedCampaignStatus() {
    List<String> mockedStatuses = Arrays.asList("Pending", "Running", "Done", "Error", "Timeout");
    var random = new SecureRandom();
    var index = random.nextInt(mockedStatuses.size());
    return mockedStatuses.get(index);
  }
}
