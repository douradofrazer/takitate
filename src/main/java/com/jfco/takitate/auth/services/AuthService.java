package com.jfco.takitate.auth.services;

import com.jfco.takitate.common.dtos.request.Login;
import com.jfco.takitate.common.dtos.request.User;
import com.jfco.takitate.common.dtos.response.KeyCloakAccessToken;

public interface AuthService {
  KeyCloakAccessToken generateAdminAccessToken();

  boolean createUser(User user);

  KeyCloakAccessToken generateAccessToken(Login userLogin);
}
