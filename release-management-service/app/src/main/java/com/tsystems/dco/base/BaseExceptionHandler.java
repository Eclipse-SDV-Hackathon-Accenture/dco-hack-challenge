package com.tsystems.dco.base;

import com.tsystems.dco.exception.IdNotFoundException;
import lombok.Generated;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.naming.AuthenticationException;
import java.nio.file.AccessDeniedException;
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

  @SneakyThrows
  @ExceptionHandler(Exception.class)
  public ResponseEntity<Map<String, Object>> handleExceptions(Exception exception) {
    log.warn("exception is : !! {}", exception.getMessage());

    var message = exception.getMessage();
    var status = HttpStatus.INTERNAL_SERVER_ERROR;
    var body = new HashMap<String, Object>();

    int startIndex = message.indexOf("{");
    int endIndex = message.indexOf("}") + 2;

    if (startIndex != -1 && endIndex != -1) {
      var substring = message.substring(startIndex, endIndex);
      log.warn("substring is {} ", substring);

      var json = new JSONObject(substring);
      if (json.has(KEY_MESSAGE))
        message = json.getString(KEY_MESSAGE);
      if (json.has(KEY_STATUS)) {
        log.warn("json.getString(KEY_STATUS) {}", json.getString(KEY_STATUS));
        var keyStatus = json.getString(KEY_STATUS);
        status = HttpStatus.valueOf(keyStatus);
      }
    }

    body.put(KEY_MESSAGE, message);
    body.put(KEY_STATUS, status);
    log.warn("Exception occurred! {}", message);
    log.warn("Exception content. {}", exception.getMessage());
    log.warn("status is : {}", status);

    return ResponseEntity.status(status).body(body);
  }

  /**
   * Security exception handler.
   *
   * @param exception the exception with message
   * @return the response with status unauthorized
   */
  @ExceptionHandler({
    AccessDeniedException.class,
    AuthenticationException.class
  })
  public ResponseEntity<Map<String, Object>> handleSecurityExceptions(Exception exception) {
    log.warn("Security exception occurred. {}", exception.getMessage());
    var status = exception instanceof AccessDeniedException
      ? HttpStatus.FORBIDDEN
      : HttpStatus.UNAUTHORIZED;
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
    body.put(KEY_MESSAGE, exception instanceof BindException bindException
      ? bindException.getBindingResult().getAllErrors().stream()
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
   * Base exception handler.
   *
   * @param exception the base exception with message
   * @return the response with status of base exception
   */
  @ExceptionHandler(BaseException.class)
  public ResponseEntity<Map<String, Object>> handleBaseExceptions(BaseException exception) {
    log.warn("Core exception occurred. {}", exception.getMessage());
    var body = new HashMap<String, Object>();
    body.put(KEY_STATUS, exception.getStatus());
    body.put(KEY_MESSAGE, exception.getMessage());
    return ResponseEntity.status(exception.getStatus()).body(body);
  }

  @ExceptionHandler(IdNotFoundException.class)
  public ResponseEntity<Map<String, Object>> handleInvalidInputExceptions(IdNotFoundException exception) {

    var body = new HashMap<String, Object>();
    var status = HttpStatus.valueOf(exception.getStatus().name());
    body.put(KEY_STATUS, exception.getStatus().name());
    body.put(KEY_MESSAGE, exception.getMessage());
    log.warn("IdNotFoundException occurred. {}", exception.getMessage());
    log.warn("IdNotFoundException content.{}, {}", HttpStatus.valueOf(exception.getStatus().name()));
    log.warn("status is : {} ",status);
    return ResponseEntity.status(status).body(body);
  }
}
