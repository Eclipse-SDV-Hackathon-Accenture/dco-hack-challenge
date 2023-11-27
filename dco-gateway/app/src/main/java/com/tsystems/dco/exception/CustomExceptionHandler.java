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

package com.tsystems.dco.exception;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CustomExceptionHandler extends DataFetcherExceptionResolverAdapter {
  private static final String KEY_STATUS = "status";
  private static final String KEY_MESSAGE = "message";

  /**
   * @param ex  the exception to resolve
   * @param env the environment for the invoked {@code DataFetcher}
   * @return GraphQLError
   */
  @SneakyThrows
  @Override
  public GraphQLError resolveToSingleError(Throwable ex, DataFetchingEnvironment env) {

    log.warn("exception is :  {}", ex.getMessage());
    var message = ex.getMessage();
    var errorType = ErrorType.INTERNAL_ERROR;

    var startIndex = message.indexOf("{");
    var endIndex = message.indexOf("}") + 2;
    if (startIndex != -1) {
      var substring = message.substring(startIndex, endIndex);

      var json = new JSONObject(substring);
      if (json.has(KEY_MESSAGE))
        message = json.getString(KEY_MESSAGE);
      if (json.has(KEY_STATUS))
        errorType = ErrorType.valueOf(json.getString(KEY_STATUS));
      log.warn("GetMessage is : {}", message);
      log.warn("GetCause is : {}", errorType);
    }
    log.warn(" ErrorType.valueOf(keyStatus) {}" + errorType);
    if (ex instanceof Exception) {
      return GraphqlErrorBuilder.newError()
        .errorType(errorType)
        .message(message)
        .path(env.getExecutionStepInfo().getPath())
        .location(env.getField().getSourceLocation())
        .build();
    }

    return null;

  }
}
