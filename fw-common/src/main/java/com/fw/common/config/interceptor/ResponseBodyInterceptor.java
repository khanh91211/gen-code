package com.fw.common.config.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.fw.common.util.LoggingUtil;

import lombok.extern.log4j.Log4j2;

@ControllerAdvice
@Log4j2
public class ResponseBodyInterceptor implements ResponseBodyAdvice<Object> {
  @Autowired
  private LoggingUtil loggingUtil;

  @Override
  public boolean supports(MethodParameter returnType,
      Class<? extends HttpMessageConverter<?>> converterType) {
    return true;
  }

  @Override
  public Object beforeBodyWrite(Object body, MethodParameter returnType,
      MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType,
      ServerHttpRequest request, ServerHttpResponse response) {
    loggingUtil.displayResp(log, ((ServletServerHttpRequest) request).getServletRequest(),
        ((ServletServerHttpResponse) response).getServletResponse(), body);
    return body;
  }

}