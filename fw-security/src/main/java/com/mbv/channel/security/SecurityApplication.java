package com.fw.channel.security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.fw.core")
@ComponentScan(basePackages = "com.fw")
@EntityScan(basePackages = "com.fw.*")
@EnableJpaRepositories(basePackages = "com.fw.*")
@ComponentScan(basePackages = { "com.fw.channel.security", "com.fw.core" })
public class SecurityApplication {
    public static void main(String[] args) {
        SpringApplication.run(SecurityApplication.class, args);
    }
}
