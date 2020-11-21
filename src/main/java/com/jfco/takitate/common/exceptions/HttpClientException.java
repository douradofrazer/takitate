package com.jfco.takitate.common.exceptions;

import org.springframework.http.HttpStatus;

public class HttpClientException extends TakitateException {

  public HttpClientException() {
    super();
  }

  public HttpClientException(HttpStatus httpStatus, String msg) {
    super(httpStatus, msg);
  }
}
