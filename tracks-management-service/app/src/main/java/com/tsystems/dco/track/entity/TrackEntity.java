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

package com.tsystems.dco.track.entity;

import com.tsystems.dco.vehicle.entity.VehicleEntity;
import lombok.*;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Builder(toBuilder = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners({
  AuditingEntityListener.class
})
@Entity(name = "track")
public class TrackEntity {
  private static final long serialVersionUID = -4602210571656981182L;
  @Id
  @GeneratedValue
  @Column(name = "id")
  private UUID id;

  @Column(name = "name")
  private String name;

  @Column(name = "track_type")
  private String trackType;

  @Column(name = "state")
  private String state;

  @Column(name = "duration")
  private String duration;

  @Column(name = "description")
  private String description;

  @LastModifiedDate
  @Column(name = "created_at")
  private Instant createdAt;

  @OneToMany(targetEntity = VehicleEntity.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  @JoinColumn(name = "track_id", referencedColumnName = "id")
  private List<VehicleEntity> vehicles;

}
