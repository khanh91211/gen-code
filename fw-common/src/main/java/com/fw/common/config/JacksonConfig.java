package com.fw.common.config;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.module.SimpleSerializers;
import com.fasterxml.jackson.databind.ser.std.CalendarSerializer;
import com.fasterxml.jackson.databind.ser.std.DateSerializer;

@Configuration
public class JacksonConfig extends SimpleModule {
  private static final long serialVersionUID = 7761550456481622275L;

  @Autowired
  private SimpleDateFormat sdf;

  @Override
  public void setupModule(SetupContext context) {
    SimpleSerializers serializers = new SimpleSerializers();

    serializers.addSerializer(Calendar.class, new CalendarSerializer(false, sdf));
    serializers.addSerializer(Date.class, new DateSerializer(false, sdf));
    context.addSerializers(serializers);
  }
}
