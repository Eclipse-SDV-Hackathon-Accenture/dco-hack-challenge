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

package com.tsystems.dco.vehicle.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.io.Serializable;
import java.util.UUID;

/**
 * VehicleEntity
 */
@Builder(toBuilder = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "vehicle")
public class VehicleEntity implements Serializable {

  private static final long serialVersionUID = 100476035305772432L;
  @Id
  @GeneratedValue
  @Column(name = "id")
  private UUID id;

  @Column(name = "vin")
  private String vin;

  @Column(name = "country")
  private String country;

}
