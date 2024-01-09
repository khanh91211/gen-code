package com.fw.channel.security.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "keycloak.api")
@Component
@Getter
@Setter
public class KeycloakApiProperty {
    private String token;
    private String userinfo;
    private String logout;
    private String introspect;
}
