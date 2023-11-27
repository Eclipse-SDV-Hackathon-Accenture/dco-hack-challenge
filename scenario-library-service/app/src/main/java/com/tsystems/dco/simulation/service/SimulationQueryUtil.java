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

import com.tsystems.dco.simulation.entity.SimulationEntity;
import org.springframework.data.jpa.domain.Specification;
import com.tsystems.dco.util.Operation;
import java.util.Optional;

public class SimulationQueryUtil {

  private SimulationQueryUtil() {
  }

  /**
   * @param query
   * @param search
   * @return Specification
   */
  public static Specification<SimulationEntity> getSimulationQuerySpecification(String query, String search) {
    return Optional.ofNullable(search)
      .map(s -> Specification
        .where(SimulationQuery.Query.builder()
          .field("name").operation(Operation.LIKE).value(s)
          .build())
        .or(SimulationQuery.Query.builder()
          .field("status").operation(Operation.LIKE).value(s)
          .build())
        .or(SimulationQuery.Query.builder()
          .field("scenarioType").operation(Operation.LIKE).value(s)
          .build())
        .or(SimulationQuery.Query.builder()
          .field("createdBy").operation(Operation.LIKE).value(s)
          .build())
        .or(SimulationQuery.Query.builder()
          .field("createdAt").operation(Operation.LIKE).value(s)
          .build()))
      .orElse(new SimulationQuery(query).toSpecification());
  }
}
