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

package com.tsystems.dco.simulation.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import com.tsystems.dco.model.ScenarioType;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners({
  AuditingEntityListener.class
})
@Entity(name = "simulation")
public class SimulationEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id")
  private UUID id;

  @Column(name = "name")
  private String name;

  @Column(name = "environment")
  private String environment;

  @Column(name = "platform")
  private String platform;

  @Column(name = "scenario_type")
  private ScenarioType scenarioType;

  @Column(name = "status")
  private String status;

  @Column(name = "hardware")
  private String hardware;

  @Column(name = "description")
  private String description;

  @CreatedDate
  @Column(name = "created_at")
  private Instant createdAt;

  @Column(name = "tracks")
  @ElementCollection(targetClass = UUID.class)
  private List<UUID> tracks;

  @Column(name = "scenarios")
  @ElementCollection(targetClass = UUID.class)
  private List<UUID> scenarios;

  @Column(name = "campaign_id")
  private UUID campaignId;

  @CreatedDate
  @Column(name = "start_date")
  private Instant startDate;

  @Column(name = "created_by")
  private String createdBy;

}
