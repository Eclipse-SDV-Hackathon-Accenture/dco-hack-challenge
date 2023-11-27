package com.tsystems.dco.base;

import lombok.Generated;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

/**
 * Base exception that contains a http status code for {@link BaseExceptionHandler}.
 */
@Getter
@ToString
@Generated
public class BaseException extends RuntimeException {

  private final HttpStatus status;

  /**
   * Base exception constructor with status and message.
   *
   * @param status The http status that should occur when exception is thrown
   * @param message The message that should occur when exception is thrown
   */
  public BaseException(HttpStatus status, String message) {
    super(message);
    this.status = status;
  }
}
