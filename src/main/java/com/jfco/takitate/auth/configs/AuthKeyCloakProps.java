package com.jfco.takitate.auth.configs;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "auth.keycloak")
public class AuthKeyCloakProps {

  private final Master master;

  private final RecipeBook recipeBook;

  public AuthKeyCloakProps() {
    master = new Master();
    recipeBook = new RecipeBook();
  }

  public AuthKeyCloakProps(Master master, RecipeBook recipeBook) {
    this.master = master;
    this.recipeBook = recipeBook;
  }

  @Data
  public static class Master {
    private String clientId;
    private String userName;
    private String password;
    private String grantType;
    private String tokenUrl;
  }

  @Data
  public static class RecipeBook {
    private String clientId;
    private String grantType;
    private String tokenUrl;
    private String userUrl;
  }
}
