package com.fw.common.config.interceptor;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fw.common.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;
import org.springframework.web.servlet.HandlerInterceptor;

import com.fw.common.util.LoggingUtil;

import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
public class RequestHandlerInterceptor implements HandlerInterceptor {
  @Autowired
  private LoggingUtil loggingUtil;

  @PostConstruct
  public void init() {
    String ip = CommonUtil.getLocalIp();
    System.setProperty("localIpValue", ip);
  }

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {
    if (request.getMethod().equals(HttpMethod.GET.name())
        || request.getMethod().equals(HttpMethod.DELETE.name())
        || request.getMethod().equals(HttpMethod.PUT.name())) {
      loggingUtil.displayReq(log, request, null);
    } else if (request instanceof StandardMultipartHttpServletRequest) {
      loggingUtil.displayReq(log, request, null);
    }
    return HandlerInterceptor.super.preHandle(request, response, handler);
  }
}
