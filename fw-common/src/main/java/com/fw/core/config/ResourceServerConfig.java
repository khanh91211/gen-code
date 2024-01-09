package com.fw.core.config;

import com.fw.core.config.adapter.RedisAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collection;

@EnableGlobalMethodSecurity(prePostEnabled = true)
@Configuration
public class ResourceServerConfig {
    private final RedisAdapter redisAdapter;

    @Autowired
    public ResourceServerConfig(RedisAdapter redisAdapter){
        this.redisAdapter=redisAdapter;
    }

    @Bean
    Converter<Jwt, Collection<GrantedAuthority>> customerJwtGrantedAuthoritiesConverter() {
        return new CustomJwtGrantedAuthoritiesConverter();
    }

    @Bean
    JwtAuthenticationConverter customJwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(customerJwtGrantedAuthoritiesConverter());
        return converter;
    }

    @Bean
    SecurityFilterChain oauth2AuthFilterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests().anyRequest().authenticated()
                .and().addFilterAfter(new CustomTokenFilter(this.redisAdapter), BearerTokenAuthenticationFilter.class).logout()
        .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and().oauth2ResourceServer().jwt().jwtAuthenticationConverter(customJwtAuthenticationConverter());
    return http.build();
    }

    @Bean
    WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring()
                .antMatchers("/actuator/health")
                .antMatchers("/v1.0/auth/generate-token")
                .antMatchers("/v1.0/bl/**")
                .antMatchers("/swagger-ui","/swagger-ui/**","/swagger-resources/**","/v2/api-docs");
    }

}
