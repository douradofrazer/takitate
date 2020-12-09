package com.jfco.takitate.common.exceptions;

import com.jfco.takitate.common.constants.Error;
import org.springframework.http.HttpStatus;

public class HttpClientException extends TakitateException {

  public HttpClientException() {
    super();
  }

  public HttpClientException(HttpStatus httpStatus, Error error, String msg) {
    super(httpStatus, error, msg);
  }
}
