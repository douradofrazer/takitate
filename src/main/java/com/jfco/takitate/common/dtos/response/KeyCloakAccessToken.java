package com.jfco.takitate.common.dtos.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class KeyCloakAccessToken {

  @JsonProperty("access_token")
  private String accessToken;

  @JsonProperty("expires_in")
  private Integer expiresIn;

  @JsonProperty("refresh_expires_in")
  private Integer refreshExpiresIn;

  @JsonProperty("refresh_token")
  private String refreshToken;

  @JsonProperty("token_type")
  private String tokenType;

  @JsonProperty("not-before-policy")
  private Integer notBeforePolicy;

  @JsonProperty("session_state")
  private String sessionState;

  private String scope;
}
