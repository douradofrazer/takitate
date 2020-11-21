package com.jfco.takitate.common.dtos.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class KeyCloakUser {
  @JsonProperty("username")
  private String userName;

  private String firstName;
  private String lastName;
  private String email;
  private boolean emailVerified;
  private boolean enabled;
  private List<Credential> credentials;
  private List<String> realmRoles;

  public KeyCloakUser() {
    this.emailVerified = true;
    this.enabled = true;
    List<String> roles = new ArrayList<>();
    roles.add("user");
    this.realmRoles = roles;
  }

  @Data
  public static class Credential {
    private String value;
    private boolean temporary;

    public Credential() {
      this.temporary = false;
    }

    public Credential(String value) {
      this.value = value;
      this.temporary = false;
    }
  }
}
