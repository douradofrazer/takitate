package com.jfco.takitate.auth.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jfco.takitate.auth.services.AuthService;
import com.jfco.takitate.common.dtos.request.Login;
import com.jfco.takitate.common.dtos.request.User;
import com.jfco.takitate.common.dtos.response.KeyCloakAccessToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringJUnitConfig
@WebMvcTest(
    controllers = {AuthController.class},
    excludeAutoConfiguration = {SecurityAutoConfiguration.class})
@TestPropertySource(properties = "keycloak.enabled=false")
class AuthControllerTest {

  public static final String API_V1_AUTH_LOGIN = "/api/v1/auth/login";

  public static final String API_V1_AUTH_USERS = "/api/v1/auth/users";

  @Autowired MockMvc mockMvc;

  @Autowired ObjectMapper objectMapper;

  @MockBean AuthService authService;

  private Login userLogin;

  private User user;

  @BeforeEach
  void setup() {
    userLogin = new Login();
    userLogin.setUserName("DUMMY");
    userLogin.setPassword("PASS");

    user = new User();
    user.setUserName("DUMMY");
    user.setFirstName("John");
    user.setLastName("Doe");
    user.setEmail("dummy@yopmail.com");
    user.setPassword("PASS");
  }

  @DisplayName("BadRequest should be thrown if username is missing for sign-up")
  @Test
  void userSignupShouldThrowExceptionIfUserNameMissing() throws Exception {

    user.setUserName(null);

    this.mockMvc
        .perform(
            post(API_V1_AUTH_USERS)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(user)))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.id", is("100001")))
        .andExpect(jsonPath("$.code", is("BAD_REQUEST")))
        .andExpect(jsonPath("$.fields['userName']", is("username is required!")));
  }

  @DisplayName("Success response should be returned on user sign-up even if first name is missing")
  @Test
  void userSignupShouldBeSuccessfulForMissingFirstName() throws Exception {

    user.setFirstName(null);

    this.mockMvc
        .perform(
            post(API_V1_AUTH_USERS)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(user)))
        .andDo(print())
        .andExpect(status().isCreated());
  }

  @DisplayName("Success response should be returned on user sign-up even if last name is missing")
  @Test
  void userSignupShouldBeSuccessfulForMissingLastName() throws Exception {

    user.setLastName(null);

    this.mockMvc
        .perform(
            post(API_V1_AUTH_USERS)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(user)))
        .andDo(print())
        .andExpect(status().isCreated());
  }

  @DisplayName("BadRequest should be thrown if email is missing for sign-up")
  @Test
  void userSignupShouldThrowExceptionIfEmailMissing() throws Exception {

    user.setEmail(null);

    this.mockMvc
        .perform(
            post(API_V1_AUTH_USERS)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(user)))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.id", is("100001")))
        .andExpect(jsonPath("$.code", is("BAD_REQUEST")))
        .andExpect(jsonPath("$.fields['email']", is("email is required!")));
  }

  @DisplayName("BadRequest should be thrown if password is missing for sign-up")
  @Test
  void userSignupShouldThrowExceptionIfPasswordMissing() throws Exception {

    user.setPassword(null);

    this.mockMvc
        .perform(
            post(API_V1_AUTH_USERS)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(user)))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.id", is("100001")))
        .andExpect(jsonPath("$.code", is("BAD_REQUEST")))
        .andExpect(jsonPath("$.fields['password']", is("password is required!")));
  }

  @DisplayName("Success response should be returned on valid user sign-up")
  @Test
  void userSignupShouldGiveSuccessResponseOnUserCreation() throws Exception {

    this.mockMvc
        .perform(
            post(API_V1_AUTH_USERS)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(user)))
        .andDo(print())
        .andExpect(status().isCreated());
  }

  @DisplayName("BadRequest should be thrown if userName is missing for sign-in")
  @Test
  void loginUserShouldThrowExceptionIfUserNameMissing() throws Exception {
    userLogin.setUserName(null);

    this.mockMvc
        .perform(
            post(API_V1_AUTH_LOGIN)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(userLogin)))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.id", is("100001")))
        .andExpect(jsonPath("$.code", is("BAD_REQUEST")))
        .andExpect(jsonPath("$.fields['userName']", is("user name is required!")));
  }

  @DisplayName("BadRequest should be thrown if password is missing for sign-in")
  @Test
  void loginUserShouldThrowExceptionIfPasswordMissing() throws Exception {
    userLogin.setPassword(null);

    this.mockMvc
        .perform(
            post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(userLogin)))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.id", is("100001")))
        .andExpect(jsonPath("$.code", is("BAD_REQUEST")))
        .andExpect(jsonPath("$.fields['password']", is("password is required!")));
  }

  @DisplayName("Generate access token is valid user login is provided for sign-in")
  @Test
  void loginUserIfValidUserCredentials() throws Exception {

    KeyCloakAccessToken accessToken = new KeyCloakAccessToken();
    accessToken.setAccessToken("DUMMY_TOKEN");

    when(authService.generateAccessToken(userLogin)).thenReturn(accessToken);

    this.mockMvc
        .perform(
            post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(userLogin)))
        .andDo(print())
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.access_token", notNullValue()))
        .andExpect(jsonPath("$.access_token", is("DUMMY_TOKEN")));
  }
}
