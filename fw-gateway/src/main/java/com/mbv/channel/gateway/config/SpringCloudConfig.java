package com.fw.channel.gateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringCloudConfig {
    @Autowired
    GatewayRouteConfig gatewayRouteConfig;

    @Bean
    public RouteLocator gatewayRoute(RouteLocatorBuilder builder) {
        RouteLocatorBuilder.Builder build = builder.routes();

        gatewayRouteConfig.getRoutes().forEach(customRoute -> build.route(
                customRoute.getId(),
                route -> route.path(customRoute.getPredicates())
                        .uri(customRoute.getUri())));

        return build.build();
    }
}
