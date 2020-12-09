package com.jfco.takitate.auth.services;

import com.jfco.takitate.auth.configs.AuthKeyCloakProps;
import com.jfco.takitate.common.constants.Error;
import com.jfco.takitate.common.dtos.request.KeyCloakUser;
import com.jfco.takitate.common.dtos.request.Login;
import com.jfco.takitate.common.dtos.request.User;
import com.jfco.takitate.common.dtos.response.KeyCloakAccessToken;
import com.jfco.takitate.common.exceptions.HttpClientException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Slf4j
@Service
public class KeyCloakProxy implements AuthService {

  private final RestTemplate restTemplate;

  private final AuthKeyCloakProps authKeyCloakProps;

  public KeyCloakProxy(RestTemplate restTemplate, AuthKeyCloakProps authKeyCloakProps) {
    this.restTemplate = restTemplate;
    this.authKeyCloakProps = authKeyCloakProps;
  }

  @Override
  public KeyCloakAccessToken generateAdminAccessToken() {

    final String tokenEndpoint = authKeyCloakProps.getMaster().getTokenUrl();

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

    MultiValueMap<String, String> formBody = new LinkedMultiValueMap<>();
    formBody.add("client_id", authKeyCloakProps.getMaster().getClientId());
    formBody.add("username", authKeyCloakProps.getMaster().getUserName());
    formBody.add("password", authKeyCloakProps.getMaster().getPassword());
    formBody.add("grant_type", authKeyCloakProps.getMaster().getGrantType());

    HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(formBody, headers);
    ResponseEntity<KeyCloakAccessToken> tokenResponse;
    try {
      tokenResponse = restTemplate.postForEntity(tokenEndpoint, request, KeyCloakAccessToken.class);
      return tokenResponse.getBody();
    } catch (HttpStatusCodeException sce) {
      log.error("Unable to generate admin token : {}", sce.getResponseBodyAsString());
      throw new HttpClientException(sce.getStatusCode(), Error.KEYCLOAK, sce.getMessage());
    }
  }

  @Override
  public boolean createUser(User user) {

    final String userCreateEndpoint = authKeyCloakProps.getRecipeBook().getUserUrl();

    HttpHeaders headers = new HttpHeaders();

    headers.add("Authorization", "Bearer " + this.generateAdminAccessToken().getAccessToken());

    KeyCloakUser kcUser = new KeyCloakUser();
    kcUser.setUserName(user.getUserName());
    kcUser.setFirstName(user.getFirstName());
    kcUser.setLastName(user.getLastName());
    kcUser.setEmail(user.getEmail());
    kcUser.setCredentials(
        Collections.singletonList(new KeyCloakUser.Credential(user.getPassword())));

    HttpEntity<KeyCloakUser> request = new HttpEntity<>(kcUser, headers);

    try {
      restTemplate.postForEntity(userCreateEndpoint, request, String.class);
      return true;
    } catch (HttpStatusCodeException sce) {
      log.error("Unable to create new user : {}", sce.getResponseBodyAsString());
      throw new HttpClientException(sce.getStatusCode(), Error.KEYCLOAK, sce.getMessage());
    }
  }

  @Override
  public KeyCloakAccessToken generateAccessToken(Login userLogin) {

    final String tokenEndpoint = authKeyCloakProps.getRecipeBook().getTokenUrl();

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

    MultiValueMap<String, String> formBody = new LinkedMultiValueMap<>();
    formBody.add("client_id", authKeyCloakProps.getRecipeBook().getClientId());
    formBody.add("username", userLogin.getUserName());
    formBody.add("password", userLogin.getPassword());
    formBody.add("grant_type", authKeyCloakProps.getRecipeBook().getGrantType());

    HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(formBody, headers);

    ResponseEntity<KeyCloakAccessToken> tokenResponse;
    try {
      tokenResponse = restTemplate.postForEntity(tokenEndpoint, request, KeyCloakAccessToken.class);
      return tokenResponse.getBody();
    } catch (HttpStatusCodeException sce) {
      log.error("Unable to login user and generate authToken : {}", sce.getResponseBodyAsString());
      throw new HttpClientException(sce.getStatusCode(), Error.KEYCLOAK, sce.getMessage());
    }
  }
}
