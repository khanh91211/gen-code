package com.fw.channel.security.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "keycloak.client")
@Component
@Getter
@Setter
public class KeycloakClientProperty {
    private String id;
    private String secret;
}
