package com.jfco.takitate.common.constants;

public enum Error {
  SERVER_ERROR("100000", "SERVER_ERROR"),
  BAD_REQUEST("100001", "BAD_REQUEST"),
  KEYCLOAK("100100", "KEYCLOAK_ERROR");

  final String id;
  final String code;

  Error(String id, String code) {
    this.id = id;
    this.code = code;
  }

  public String getId() {
    return id;
  }

  public String getCode() {
    return code;
  }
}
