package com.jfco.takitate.common.exceptions;

import com.jfco.takitate.common.constants.Error;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class TakitateException extends RuntimeException {
  protected final HttpStatus httpStatus;
  protected final Error error;
  protected final String msg;

  public TakitateException() {
    this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    this.error = Error.SERVER_ERROR;
    this.msg = "Oops something went wrong!";
  }

  public TakitateException(HttpStatus httpStatus, Error error, String msg) {
    this.httpStatus = httpStatus;
    this.error = error;
    this.msg = msg;
  }
}
