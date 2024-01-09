package com.fw.core.config;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fw.model.Constants;

@Configuration
public class CommonBeanConfig {
  /**
   * Global date format, support ISO 8601 with GMT+7 timezone
   * 
   * @return the bean
   */
  @Bean
  SimpleDateFormat simpleDateFormat() {
    SimpleDateFormat sdf = new SimpleDateFormat(Constants.DEFAULT_DATE_FORMAT);
    sdf.setTimeZone(TimeZone.getTimeZone(Constants.DEFAULT_TIME_ZONE));
    return sdf;
  }

  /**
   * Global object mapper, support ignore unknown properties
   * 
   * @return the bean
   */
  @Bean
  ObjectMapper objectMapper() {
    ObjectMapper om = new ObjectMapper();
    om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    return om;
  }
}
