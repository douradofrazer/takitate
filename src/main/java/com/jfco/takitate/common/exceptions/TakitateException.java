package com.jfco.takitate.common.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class TakitateException extends RuntimeException {
  protected final HttpStatus httpStatus;
  protected final String msg;

  public TakitateException() {
    this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    this.msg = "Oops something went wrong!";
  }

  public TakitateException(HttpStatus httpStatus, String msg) {
    this.httpStatus = httpStatus;
    this.msg = msg;
  }
}
