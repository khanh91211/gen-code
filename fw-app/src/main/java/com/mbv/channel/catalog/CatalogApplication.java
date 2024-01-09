package com.fw.channel.catalog;

import com.fw.core.integration.ChCatalogClient;
import com.fw.core.integration.CommonCacheService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@SpringBootApplication
@EntityScan(basePackages = "com.fw.*")
@ComponentScan(basePackages = {"com.fw.channel.catalog", "com.fw.core"}, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = {ChCatalogClient.class, CommonCacheService.class})})
public class CatalogApplication {
    public static void main(String[] args) {
        SpringApplication.run(CatalogApplication.class, args);
    }
}
