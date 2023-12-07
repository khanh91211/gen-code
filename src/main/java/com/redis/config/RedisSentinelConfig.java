package com.redis.config;

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
    private List<RedisAddress> address;

    @Data
    public static class RedisAddress {
        private String uri;
    }
    private String password;
    private String masterName;
    private int database;
    private int maxTotal;
    private int maxIdle;
    private int minIdle;
    private int maxWait;
    private int timeout;
}
