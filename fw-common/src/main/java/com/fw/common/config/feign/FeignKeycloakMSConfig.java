package com.fw.common.config.feign;

import feign.RequestInterceptor;
import feign.codec.ErrorDecoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.security.oauth2.client.feign.OAuth2FeignRequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;

@SuppressWarnings("deprecation")
public class FeignKeycloakMSConfig {

    @Value("${integration.il.oauth2.accessTokenUri}")
    private String accessTokenUri;
    @Value("${integration.il.oauth2.default-client-id}")
    private String msClientId;
    @Value("${integration.il.oauth2.default-client-secret}")
    private String msClientSecret;

    @Bean
    RequestInterceptor oauth2FeignRequestInterceptor() {
        return new OAuth2FeignRequestInterceptor(new DefaultOAuth2ClientContext(), resource());
    }

    private OAuth2ProtectedResourceDetails resource() {
        var resourceDetails = new ClientCredentialsResourceDetails();
        resourceDetails.setAccessTokenUri(accessTokenUri);
        resourceDetails.setClientId(msClientId);
        resourceDetails.setClientSecret(msClientSecret);
        resourceDetails.setGrantType("client_credentials");
        return resourceDetails;
    }

    @Bean
    ErrorDecoder errorDecoder() {
        return new IlT24CustomErrorDecoder();
    }
}
