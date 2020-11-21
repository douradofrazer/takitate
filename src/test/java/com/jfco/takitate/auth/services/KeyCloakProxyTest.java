package com.jfco.takitate.auth.services;

import com.jfco.takitate.auth.configs.AuthKeyCloakProps;
import com.jfco.takitate.common.dtos.request.Login;
import com.jfco.takitate.common.dtos.request.User;
import com.jfco.takitate.common.dtos.response.KeyCloakAccessToken;
import com.jfco.takitate.common.exceptions.HttpClientException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class KeyCloakProxyTest {

  @Mock RestTemplate restTemplate;

  @InjectMocks KeyCloakProxy keyCloakProxy;

  @BeforeEach
  void setup() {
    AuthKeyCloakProps.Master master = new AuthKeyCloakProps.Master();
    master.setTokenUrl("DUMMY_URL");
    master.setClientId("DUMMY_CLIENT");
    master.setGrantType("PASSWORD");
    AuthKeyCloakProps.RecipeBook recipeBook = new AuthKeyCloakProps.RecipeBook();
    recipeBook.setTokenUrl("DUMMY_URL");
    recipeBook.setUserUrl("DUMMY_USER_URL");
    recipeBook.setClientId("DUMMY_URL");
    recipeBook.setGrantType("PASSWORD");
    AuthKeyCloakProps keyCloakProps = new AuthKeyCloakProps(master, recipeBook);
    ReflectionTestUtils.setField(keyCloakProxy, "authKeyCloakProps", keyCloakProps);

    KeyCloakAccessToken accessToken = new KeyCloakAccessToken();
    accessToken.setAccessToken("DUMMY_TOKEN");

    when(restTemplate.postForEntity(anyString(), any(), eq(KeyCloakAccessToken.class)))
        .thenReturn(new ResponseEntity<>(accessToken, HttpStatus.OK));
  }

  @AfterEach
  void tearDown() {
    reset(restTemplate);
  }

  @DisplayName(
      "Generate authToken for admin should throw HttpClientException for any failure while communicating with keycloak")
  @Test
  void generateAdminAccessTokenThrowsHttpClientException() {

    KeyCloakAccessToken accessToken = new KeyCloakAccessToken();
    accessToken.setAccessToken("DUMMY_TOKEN");

    when(restTemplate.postForEntity(anyString(), any(), eq(KeyCloakAccessToken.class)))
        .thenThrow(new HttpClientErrorException(HttpStatus.UNAUTHORIZED));

    HttpClientException httpClientException =
        assertThrows(HttpClientException.class, () -> keyCloakProxy.generateAdminAccessToken());

    assertThat(httpClientException.getHttpStatus()).isEqualTo(HttpStatus.UNAUTHORIZED);
  }

  @DisplayName("Generate authToken for admin successfully")
  @Test
  void generateAdminAccessToken() {

    KeyCloakAccessToken token = keyCloakProxy.generateAdminAccessToken();

    assertThat(token.getAccessToken()).isEqualTo("DUMMY_TOKEN");
  }

  @DisplayName(
      "Create user should throw HttpClientException if any failure in communication with keycloak")
  @Test
  void createUserShouldThrowHttpClientException() {

    when(restTemplate.postForEntity(anyString(), any(), eq(String.class)))
        .thenThrow(new HttpClientErrorException(HttpStatus.UNAUTHORIZED));

    HttpClientException httpClientException =
        assertThrows(HttpClientException.class, () -> keyCloakProxy.createUser(new User()));

    assertThat(httpClientException.getHttpStatus()).isEqualTo(HttpStatus.UNAUTHORIZED);
  }

  @DisplayName("Create user should return true if created successfully with keycloak")
  @Test
  void createUser() {

    when(restTemplate.postForEntity(anyString(), any(), eq(String.class)))
        .thenReturn(new ResponseEntity<>(HttpStatus.CREATED));

    boolean isUserCreated = keyCloakProxy.createUser(new User());

    assertThat(isUserCreated).isTrue();
  }

  @DisplayName(
      "Login user should throw HttpClientException if any failure in communication with keycloak")
  @Test
  void loginUserShouldThrowHttpClientException() {

    Login userLogin = new Login();
    userLogin.setUserName("DUMMY_USER");
    userLogin.setPassword("PASS_INVALID");

    when(restTemplate.postForEntity(anyString(), any(), eq(KeyCloakAccessToken.class)))
        .thenThrow(new HttpClientErrorException(HttpStatus.UNAUTHORIZED));

    HttpClientException httpClientException =
        assertThrows(HttpClientException.class, () -> keyCloakProxy.generateAccessToken(userLogin));

    assertThat(httpClientException.getHttpStatus()).isEqualTo(HttpStatus.UNAUTHORIZED);
  }

  @DisplayName("Login user should generate accessToken if it's a successful login with keycloak")
  @Test
  void loginUser() {

    Login userLogin = new Login();
    userLogin.setUserName("DUMMY_USER");
    userLogin.setPassword("PASS");

    KeyCloakAccessToken accessToken = keyCloakProxy.generateAccessToken(userLogin);

    assertThat(accessToken.getAccessToken()).isEqualTo("DUMMY_TOKEN");
  }
}
