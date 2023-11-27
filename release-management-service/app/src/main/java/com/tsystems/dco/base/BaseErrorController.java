package com.tsystems.dco.base;

import java.util.Collections;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import lombok.Generated;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Base error controller that returns exceptions always as application/json response to requests.
 */
@Slf4j
@Getter
@Setter
@Generated
@Controller
@RequestMapping("${server.error.path:/error}")
@ConfigurationProperties(prefix = "server.error")
public class BaseErrorController extends AbstractErrorController {

  /**
   * Enumeration to define when an attribute should be included.
   */
  public enum IncludeAttribute {
    NEVER,
    ALWAYS,
    ON_PARAM
  }

  private boolean includeException;
  private IncludeAttribute includeStacktrace = IncludeAttribute.NEVER;
  private IncludeAttribute includeMessage = IncludeAttribute.NEVER;
  private IncludeAttribute includeBindingErrors = IncludeAttribute.NEVER;

  public BaseErrorController(ErrorAttributes errorAttributes) {
    super(errorAttributes, Collections.emptyList());
  }

  /**
   * Basic error response controller that maps errors to json responses.
   *
   * @param request the incoming servlet request
   * @return the json error response entity
   */
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {
    var status = getStatus(request);
    if (status == HttpStatus.NO_CONTENT) {
      return new ResponseEntity<>(status);
    }
    var body = getErrorAttributes(request, getErrorAttributeOptions(request));
    return new ResponseEntity<>(body, status);
  }

  protected ErrorAttributeOptions getErrorAttributeOptions(HttpServletRequest request) {
    var options = ErrorAttributeOptions.defaults();
    if (includeException) {
      options = options.including(ErrorAttributeOptions.Include.EXCEPTION);
    }
    if (isIncludeStackTrace(request)) {
      options = options.including(ErrorAttributeOptions.Include.STACK_TRACE);
    }
    if (isIncludeMessage(request)) {
      options = options.including(ErrorAttributeOptions.Include.MESSAGE);
    }
    if (isIncludeBindingErrors(request)) {
      options = options.including(ErrorAttributeOptions.Include.BINDING_ERRORS);
    }
    return options;
  }

  /**
   * Determine if the stacktrace attribute should be included.
   *
   * @param request the source request
   * @return if the stacktrace attribute should be included
   */
  protected boolean isIncludeStackTrace(HttpServletRequest request) {
    return switch (includeStacktrace) {
      case ALWAYS -> true;
      case ON_PARAM -> getTraceParameter(request);
      default -> false;
    };
  }

  /**
   * Determine if the message attribute should be included.
   *
   * @param request the source request
   * @return if the message attribute should be included
   */
  protected boolean isIncludeMessage(HttpServletRequest request) {
    return switch (includeMessage) {
      case ALWAYS -> true;
      case ON_PARAM -> getMessageParameter(request);
      default -> false;
    };
  }

  /**
   * Determine if the errors attribute should be included.
   *
   * @param request the source request
   * @return if the errors attribute should be included
   */
  protected boolean isIncludeBindingErrors(HttpServletRequest request) {
    return switch (includeBindingErrors) {
      case ALWAYS -> true;
      case ON_PARAM -> getErrorsParameter(request);
      default -> false;
    };
  }
}
