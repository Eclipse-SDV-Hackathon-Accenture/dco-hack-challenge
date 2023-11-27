package com.tsystems.dco.exception;

import lombok.Generated;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Getter
@ToString
@Generated
public class IdNotFoundException extends RuntimeException {

  private final HttpStatus status;

  /**
   * IdNotFoundException constructor with status and message.
   *
   * @param status The http status that should occur when exception is thrown
   * @param message The message that should occur when exception is thrown
   */
  public IdNotFoundException(HttpStatus status, String message) {
    super(message);
    this.status = status;
  }
}
