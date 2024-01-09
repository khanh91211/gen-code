package com.fw.core.config.interceptor;

import java.lang.reflect.Type;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import com.fw.core.util.LoggingUtil;

import lombok.extern.log4j.Log4j2;

@ControllerAdvice
@Log4j2
public class RequestBodyInterceptor extends RequestBodyAdviceAdapter {
  @Autowired
  private LoggingUtil loggingUtil;
  @Autowired
  private HttpServletRequest request;

  @Override
  public boolean supports(MethodParameter methodParameter, Type targetType,
      Class<? extends HttpMessageConverter<?>> converterType) {
    return true;
  }

  @Override
  public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter,
      Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
    loggingUtil.displayReq(log, request, body);
    return super.afterBodyRead(body, inputMessage, parameter, targetType, converterType);
  }
}
