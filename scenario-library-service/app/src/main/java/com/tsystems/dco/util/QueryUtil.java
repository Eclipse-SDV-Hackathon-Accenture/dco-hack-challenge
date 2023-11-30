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

package com.tsystems.dco.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class QueryUtil {

  private QueryUtil(){}
  public static final Sort.Order ORDER_DEFAULT = Sort.Order.asc("createdAt");
  public static final Integer PAGEABLE_DEFAULT_PAGE = 0;
  public static final Integer PAGEABLE_DEFAULT_SIZE = 15;
  public static final Pattern QUERY_PATTERN = Pattern.compile("([\\w.]+?)([:!~])(.*?),");

  /**
   * @param page
   * @param size
   * @param sort
   * @return PageRequest
   */
  public static PageRequest getPageRequest(Integer page, Integer size, List<String> sort) {
    var orders = Optional.ofNullable(sort)
      .map(s -> s.stream()
        .map(so -> so.split(":"))
        .map(sp -> sp.length >= 2 ? new Sort.Order(Sort.Direction.fromString(sp[1]), sp[0]) : null)
        .filter(Objects::nonNull)
      ).orElse(Stream.of(QueryUtil.ORDER_DEFAULT))
      .toList();
    return PageRequest.of(
      Optional.ofNullable(page).orElse(QueryUtil.PAGEABLE_DEFAULT_PAGE),
      Optional.ofNullable(size).orElse(QueryUtil.PAGEABLE_DEFAULT_SIZE),
      Sort.by(orders)
    );
  }
}
