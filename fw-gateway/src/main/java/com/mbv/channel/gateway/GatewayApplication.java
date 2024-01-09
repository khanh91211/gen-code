package com.fw.channel.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@ComponentScan(basePackages = "com.fw.channel")
@SpringBootApplication
public class GatewayApplication extends SpringBootServletInitializer  {
  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
    return builder.sources(GatewayApplication.class);
  }
  public static void main(String[] args) {
    SpringApplication.run(GatewayApplication.class, args);
  }

  @PostConstruct
  void started() {
    TimeZone.setDefault(TimeZone.getTimeZone("GMT+7"));
  }
}
