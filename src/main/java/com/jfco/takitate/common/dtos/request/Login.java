package com.jfco.takitate.common.dtos.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class Login {
  @NotBlank(message = "user name is required!")
  private String userName;

  @NotBlank(message = "password is required!")
  private String password;
}
