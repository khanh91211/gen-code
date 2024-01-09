package com.fw.gateway.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

import java.util.List;


@ConfigurationProperties(prefix = "mb")
@Component
@Getter
@Setter
public class GatewayRouteConfig {
    private List<RouteConfig> routes;

    @Data
    public static class RouteConfig {
        private String id;
        private String uri;
        private String predicates;
    }
}
