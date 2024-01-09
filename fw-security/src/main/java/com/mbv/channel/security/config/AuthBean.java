package com.fw.channel.security.config;

import com.fw.channel.security.service.LoginService;
import com.fw.channel.security.service.impl.LoginServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;


@Configuration
class AuthBean {
    @Bean
    LoginService loginService() {
        return new LoginServiceImpl();
    }

    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
