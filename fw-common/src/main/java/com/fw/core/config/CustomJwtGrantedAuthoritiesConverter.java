package com.fw.core.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.fw.core.config.adapter.RedisAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;


public final class CustomJwtGrantedAuthoritiesConverter implements Converter<Jwt, Collection<GrantedAuthority>> {
    @Autowired
    private RedisAdapter redisAdapter;

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        String token = jwt.getTokenValue();
        String key = "authority::" + token.hashCode();
        List<?> authorities = redisAdapter.get(key, List.class);
        if (authorities != null) {
            for (Object authority : authorities) {
                grantedAuthorities.add(new SimpleGrantedAuthority((String) authority));
            }
        }
        return grantedAuthorities;
    }

}
