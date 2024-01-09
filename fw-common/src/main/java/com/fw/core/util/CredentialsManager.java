package com.fw.core.util;

import static java.util.Objects.isNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.registration.ClientRegistration;

public class CredentialsManager {

  private static final Logger logger = LoggerFactory.getLogger(CredentialsManager.class);

  private final OAuth2AuthorizedClientManager manager;
  private final Authentication principal;
  private final ClientRegistration clientRegistration;

  public CredentialsManager(OAuth2AuthorizedClientManager manager,
      ClientRegistration clientRegistration) {
    this.manager = manager;
    this.clientRegistration = clientRegistration;
    this.principal = new OauthServiceAuthentication(clientRegistration.getClientId());
  }

  public String getAccessToken() {
    try {
      OAuth2AuthorizeRequest oAuth2AuthorizeRequest = OAuth2AuthorizeRequest
          .withClientRegistrationId(clientRegistration.getRegistrationId()).principal(principal)
          .build();
      OAuth2AuthorizedClient client = manager.authorize(oAuth2AuthorizeRequest);
      if (isNull(client)) {
        throw new IllegalStateException("client credentials flow on "
            + clientRegistration.getRegistrationId() + " failed, client is null");
      }
      return client.getAccessToken().getTokenValue();
    } catch (Exception exp) {
      logger.error("client credentials error {}", exp.getMessage());
    }
    return null;
  }
}