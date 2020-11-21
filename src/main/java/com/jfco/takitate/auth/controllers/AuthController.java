package com.jfco.takitate.auth.controllers;

import com.jfco.takitate.common.dtos.request.Login;
import com.jfco.takitate.common.dtos.request.User;
import com.jfco.takitate.common.dtos.response.KeyCloakAccessToken;
import com.jfco.takitate.auth.services.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

  private final AuthService authService;

  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  @PostMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.CREATED)
  public void userSignup(@RequestBody @Validated User user) {
    authService.createUser(user);
  }

  @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.CREATED)
  public KeyCloakAccessToken loginUserAndGenerateAccessToken(
      @RequestBody @Validated Login userLogin) {

    return authService.generateAccessToken(userLogin);
  }
}
