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

package com.tsystems.dco.scenario.service;

import com.tsystems.dco.exception.BaseException;
import com.tsystems.dco.scenario.entity.ScenarioEntity;
import com.tsystems.dco.util.Operation;
import com.tsystems.dco.util.QueryUtil;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
public class ScenarioQuery {
  private final List<Query> queries = new ArrayList<>();

  public ScenarioQuery(String query) {
    var matcher = QueryUtil.QUERY_PATTERN.matcher(query + ",");
    while (matcher.find()) {
      queries.add(new Query(
        matcher.group(1),
        Operation.from(matcher.group(2).charAt(0)),
        matcher.group(3))
      );
    }
  }

  public ScenarioQuery isValid() {
    if (queries.isEmpty()) {
      throw new BaseException(HttpStatus.BAD_REQUEST, "The query does not match the valid pattern.");
    }
    return this;
  }

  public Specification<ScenarioEntity> toSpecification() {
    if (queries.isEmpty()) {
      return null;
    }
    Specification<ScenarioEntity> result = queries.get(0);
    for (var i = 1; i < queries.size(); i++) {
      result = Specification.where(result).and(queries.get(i));
    }
    return result;
  }

  @Builder
  @RequiredArgsConstructor
  public static class Query implements Serializable, Specification<ScenarioEntity> {
    private final String field;
    private final Operation operation;
    private final String value;

    @Override
    public Predicate toPredicate(
      Root<ScenarioEntity> root,
      CriteriaQuery<?> query,
      CriteriaBuilder builder
    ) {
      return switch (operation) {
        case EQUAL -> builder.equal(root.get(field), value);
        case NOT_EQUAL -> builder.notEqual(root.get(field), value);
        case LIKE -> builder.like(root.get(field), "%" + value + "%");
      };
    }
  }
}
