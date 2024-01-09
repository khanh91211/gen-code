package com.fw.common.config.feign;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.security.oauth2.client.feign.OAuth2FeignRequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;

import feign.RequestInterceptor;

/**
 * @author manhpt
 * @Date 2022
 */
@EnableFeignClients
@SuppressWarnings("deprecation")
public class FeignKeycloakChConfig {
    @Value("${ch.keycloak.accessTokenUri}")
    private String chAccessTokenUri;
    @Value("${ch.keycloak.username}")
    private String chClientId;
    @Value("${ch.keycloak.secret}")
    private String chClientSecret;

	@Bean
    RequestInterceptor oauth2FeignRequestInterceptor() {
        return new OAuth2FeignRequestInterceptor(new DefaultOAuth2ClientContext(), resource());
    }

    private OAuth2ProtectedResourceDetails resource() {
        var resourceDetails = new ClientCredentialsResourceDetails();
        resourceDetails.setAccessTokenUri(chAccessTokenUri);
        resourceDetails.setClientId(chClientId);
        resourceDetails.setClientSecret(chClientSecret);
        resourceDetails.setGrantType("client_credentials");
        return resourceDetails;
    }
}
