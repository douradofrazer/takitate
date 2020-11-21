package com.jfco.takitate.auth;

import com.jfco.takitate.auth.configs.AuthKeyCloakProps;
import com.jfco.takitate.common.dtos.request.KeyCloakUser;
import com.jfco.takitate.common.dtos.response.KeyCloakAccessToken;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

@Disabled
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ClientConnectionTest {

  @Autowired TestRestTemplate testRestTemplate;

  @Autowired
  AuthKeyCloakProps authKeyCloakProps;

  @Test
  void generateAdminToken() {

    ResponseEntity<KeyCloakAccessToken> tokenResponse = this.getKeyCloakAdminAccessToken();

    assertThat(tokenResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(tokenResponse.getStatusCodeValue()).isEqualTo(200);

    KeyCloakAccessToken token = tokenResponse.getBody();

    assertThat(token).isNotNull();
    assertThat(token.getAccessToken()).isNotBlank();
  }

  private ResponseEntity<KeyCloakAccessToken> getKeyCloakAdminAccessToken() {

    final String tokenEndpoint =
        "https://jfco-keycloak.herokuapp.com/auth/realms/master/protocol/openid-connect/token";

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

    MultiValueMap<String, String> formBody = new LinkedMultiValueMap<>();
    formBody.add("client_id", "admin-cli");
    formBody.add("username", "admin");
    formBody.add("password", "2etRufraqe");
    formBody.add("grant_type", "password");

    HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(formBody, headers);

    ResponseEntity<KeyCloakAccessToken> tokenResponse =
        testRestTemplate.postForEntity(tokenEndpoint, request, KeyCloakAccessToken.class);

    return tokenResponse;
  }

  @Test
  void generateAuthToken() {
    final String tokenEndpoint =
        "https://jfco-keycloak.herokuapp.com/auth/realms/recipebook/protocol/openid-connect/token";

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

    MultiValueMap<String, String> formBody = new LinkedMultiValueMap<>();
    formBody.add("client_id", "recipe-book");
    formBody.add("username", "json");
    formBody.add("password", "password");
    formBody.add("grant_type", "password");

    HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(formBody, headers);

    ResponseEntity<KeyCloakAccessToken> tokenResponse =
        testRestTemplate.postForEntity(tokenEndpoint, request, KeyCloakAccessToken.class);

    assertThat(tokenResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(tokenResponse.getStatusCodeValue()).isEqualTo(200);

    KeyCloakAccessToken token = tokenResponse.getBody();

    assertThat(token).isNotNull();
    assertThat(token.getAccessToken()).isNotBlank();
  }

  @Disabled
  @Test
  void createUser() {
    final String userCreateEndpoint =
        "https://jfco-keycloak.herokuapp.com/auth/admin/realms/recipebook/users";

    HttpHeaders headers = new HttpHeaders();

    headers.add(
        "Authorization", "Bearer " + this.getKeyCloakAdminAccessToken().getBody().getAccessToken());

    KeyCloakUser kcUser = new KeyCloakUser();
    kcUser.setUserName("james.may");
    kcUser.setFirstName("James");
    kcUser.setLastName("May");
    kcUser.setEmail("james.may@yopmail.com");
    kcUser.setCredentials(Collections.singletonList(new KeyCloakUser.Credential("pass123")));

    HttpEntity<KeyCloakUser> request = new HttpEntity<>(kcUser, headers);

    ResponseEntity<String> createUserResponse =
        testRestTemplate.postForEntity(userCreateEndpoint, request, String.class);

    System.out.println(createUserResponse.getBody());

    assertThat(createUserResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    assertThat(createUserResponse.getStatusCodeValue()).isEqualTo(201);

  }
}
