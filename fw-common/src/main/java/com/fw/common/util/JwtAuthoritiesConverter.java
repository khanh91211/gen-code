package com.fw.common.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.util.StringUtils;

import com.nimbusds.jose.shaded.json.JSONArray;

public final class JwtAuthoritiesConverter implements Converter<Jwt, Collection<GrantedAuthority>> {
  private static final String DEFAULT_SCOPE_AUTHORITY_PREFIX = "SCOPE_";
  private static final String DEFAULT_ROLE_AUTHORITY_PREFIX = "ROLE_";
  private static final String SCOPE_AUTHORITIES_CLAIM_NAME = "scope";
  private static final String ROLE_AUTHORITIES_CLAIM_NAME = "realm_access";

  @Override
  public Collection<GrantedAuthority> convert(Jwt jwt) {
    Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
    //XXX: get detail authorities from authorization api and cache data to redis
    for (String authority : getScopeAuthorities(jwt)) {
      grantedAuthorities
          .add(new SimpleGrantedAuthority(DEFAULT_SCOPE_AUTHORITY_PREFIX + authority));
    }
    for (String authority : getRoleAuthorities(jwt)) {
      grantedAuthorities.add(new SimpleGrantedAuthority(DEFAULT_ROLE_AUTHORITY_PREFIX + authority));
    }
    return grantedAuthorities;
  }

  @SuppressWarnings("unchecked")
  private Collection<String> getScopeAuthorities(Jwt jwt) {
    Object authorities = jwt.getClaim(SCOPE_AUTHORITIES_CLAIM_NAME);
    if (authorities instanceof String) {
      if (StringUtils.hasText((String) authorities)) {
        return Arrays.asList(((String) authorities).split(" "));
      }
      return Collections.emptyList();
    }
    if (authorities instanceof Collection) {
      return (Collection<String>) (authorities);
    }
    return Collections.emptyList();
  }

  @SuppressWarnings("unchecked")
  private Collection<String> getRoleAuthorities(Jwt jwt) {
    List<String> ret = new ArrayList<>();
    Object authorities = jwt.getClaim(ROLE_AUTHORITIES_CLAIM_NAME);
    if (authorities instanceof Map) {
      Map<String, Object> m = (Map<String, Object>) authorities;
      Object roles = m.get("roles");
      if (roles instanceof JSONArray) {
        JSONArray ja = (JSONArray) roles;
        ja.forEach(o -> ret.add(o.toString()));
      }
    }
    return ret;
  }
}
