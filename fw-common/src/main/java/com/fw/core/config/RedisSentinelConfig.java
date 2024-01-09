package com.fw.core.config;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@ConfigurationProperties(prefix = "mb.redis-sentinel")
@Component
@Getter
@Setter
public class RedisSentinelConfig {
    private int database;
    private int maxTotal;
    private int maxIdle;
    private int maxWait;
    private int timeout;
    private String masterName;
    private String masterPassword;
    private List<RedisAddress> address;

    @Data
    public static class RedisAddress {
        private String uri;
    }
}
