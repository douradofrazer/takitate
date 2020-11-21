package com.jfco.takitate.common.dtos.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class User {
  @NotBlank(message = "username is required!")
  private String userName;

  @NotBlank(message = "first name is required!")
  private String firstName;

  @NotBlank(message = "last name is required!")
  private String lastName;

  @NotBlank(message = "email is required!")
  private String email;

  @NotBlank(message = "password is required!")
  private String password;
}
