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

import lombok.Generated;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Base exception handler that returns a generic map as response to user on exception.
 */
@Slf4j
@Generated
@RestControllerAdvice
public class BaseExceptionHandler {

  private static final String KEY_STATUS = "status";
  private static final String KEY_MESSAGE = "message";

  /**
   * Default exception handler.
   *
   * @param exception the exception with message
   * @return the response with status internal server error
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<Map<String, Object>> handleExceptions(Exception exception) {
    log.warn("Exception occurred. {}", exception.getMessage());
    log.warn("Exception content.", exception);
    var status = HttpStatus.INTERNAL_SERVER_ERROR;
    var body = new HashMap<String, Object>();
    body.put(KEY_STATUS, status);
    body.put(KEY_MESSAGE, exception.getMessage());
    return ResponseEntity.status(status).body(body);
  }

  /**
   * Validation exception handler.
   *
   * @param exception the exception with message
   * @return the response with status bad request
   */
  @ExceptionHandler({
    BindException.class,
    MethodArgumentNotValidException.class
  })
  public ResponseEntity<Map<String, Object>> handleValidationExceptions(Exception exception) {
    log.warn("Validation exception occurred. {}", exception.getMessage());
    var status = HttpStatus.BAD_REQUEST;
    var body = new HashMap<String, Object>();
    body.put(KEY_STATUS, status);
    body.put(KEY_MESSAGE, exception instanceof BindException
      ? ((BindException) exception).getBindingResult().getAllErrors().stream()
      .map(error -> {
        var field = error.getCode();
        if (error instanceof FieldError fieldError) {
          field = fieldError.getField();
        }
        return String.format("%s %s", field, error.getDefaultMessage());
      })
      .collect(Collectors.joining(", "))
      : exception.getMessage());
    return ResponseEntity.status(status).body(body);
  }

  /**
   * DataNotFoundException handler.
   *
   * @param exception the base exception with message
   * @return the response with status of base exception
   */
  @ExceptionHandler(DataNotFoundException.class)
  public ResponseEntity<Map<String, Object>> handleDataNotFoundException(DataNotFoundException exception) {
    log.warn("DataNotFound exception occurred. {}", exception.getMessage());
    var body = new HashMap<String, Object>();
    body.put(KEY_STATUS, exception.getStatus());
    body.put(KEY_MESSAGE, exception.getMessage());
    return ResponseEntity.status(exception.getStatus()).body(body);
  }

  /**
   * DataDeletionException handler.
   *
   * @param exception the base exception with message
   * @return the response with status of base exception
   */
  @ExceptionHandler(DataDeletionException.class)
  public ResponseEntity<Map<String, Object>> handleDataDeletionException(DataDeletionException exception) {
    log.warn("DataDeletion exception occurred. {}", exception.getMessage());
    var body = new HashMap<String, Object>();
    body.put(KEY_STATUS, exception.getStatus());
    body.put(KEY_MESSAGE, exception.getMessage());
    return ResponseEntity.status(exception.getStatus()).body(body);
  }

}
