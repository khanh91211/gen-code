package com.fw.core.util;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

public class OauthServiceAuthentication implements Authentication {
  private static final long serialVersionUID = 598051386756041373L;
  private String clientId;

  public OauthServiceAuthentication(String clientId) {
    this.clientId = clientId;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Collections.emptySet();
  }

  @Override
  public Object getCredentials() {
    return null;
  }

  @Override
  public Object getDetails() {
    return null;
  }

  @Override
  public Object getPrincipal() {
    return this;
  }

  @Override
  public boolean isAuthenticated() {
    return false;
  }

  @Override
  public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
    //do nothing
  }

  @Override
  public String getName() {
    return clientId;
  }
}
