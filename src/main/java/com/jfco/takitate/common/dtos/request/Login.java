package com.jfco.takitate.common.dtos.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class Login {
  @NotBlank(message = "User name is missing!")
  private String userName;

  @NotBlank(message = "Password is missing!")
  private String password;
}
