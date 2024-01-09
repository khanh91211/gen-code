package com.fw.core.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "mb.redis")
@Component
@Getter
@Setter
public class RedisConfig {
    private boolean isCluster;
    private String host;
    private int port;
    private int timeout;
    private String password;
    private int database;
    private int maxTotal;
    private int maxIdle;
    private int maxWait;
}
