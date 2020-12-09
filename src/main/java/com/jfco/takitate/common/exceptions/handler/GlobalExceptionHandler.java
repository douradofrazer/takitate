package com.jfco.takitate.common.exceptions.handler;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jfco.takitate.common.constants.Error;
import com.jfco.takitate.common.exceptions.TakitateException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  private static final String DEFAULT_EXCEPTION_CAUSE_FORMAT = "Exception : {} , Cause : {}";

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> generalException(
      final HttpServletResponse response, Exception e) {

    ErrorResponse errorResponse = new ErrorResponse(Error.SERVER_ERROR);

    if (null == e.getMessage()) {
      log.error(DEFAULT_EXCEPTION_CAUSE_FORMAT, e.getClass(), e.getMessage(), e);
    } else {
      log.error(DEFAULT_EXCEPTION_CAUSE_FORMAT, e.getClass(), e.getMessage());
      errorResponse.setMessage(e.getMessage());
    }

    return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(TakitateException.class)
  public ResponseEntity<ErrorResponse> handleCustomException(
      final HttpServletResponse response, TakitateException e) {

    log.error(DEFAULT_EXCEPTION_CAUSE_FORMAT, e.getClass(), e.getMessage());

    ErrorResponse errorResponse = new ErrorResponse(e.getError());
    errorResponse.setMessage(e.getMessage());

    return new ResponseEntity<>(errorResponse, e.getHttpStatus());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidationException(
      final HttpServletResponse response, MethodArgumentNotValidException e) {

    log.error(DEFAULT_EXCEPTION_CAUSE_FORMAT, e.getClass(), e.getMessage());

    ErrorResponse errorResponse = new ErrorResponse(Error.BAD_REQUEST);

    Map<String, String> fieldErrors = new HashMap<>();

    for (FieldError error : e.getBindingResult().getFieldErrors()) {
      fieldErrors.put(error.getField(), error.getDefaultMessage());
    }
    for (ObjectError error : e.getBindingResult().getGlobalErrors()) {
      fieldErrors.put(error.getObjectName(), error.getDefaultMessage());
    }

    errorResponse.setFields(fieldErrors);

    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }

  @Data
  @JsonInclude(JsonInclude.Include.NON_NULL)
  public static class ErrorResponse {
    private String id;
    private String code;
    private String message;
    private Map<String, String> fields;

    public ErrorResponse(Error error) {
      this.id = error.getId();
      this.code = error.getCode();
      this.message = "Something went wrong!";
    }
  }
}
