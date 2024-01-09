package com.fw.common.config.interceptor;

import com.fw.common.model.RequestContext;
import com.fw.common.model.enumeration.HeaderEnum;
import com.fw.common.util.CommonUtil;
import com.fw.common.util.RequestContextHolder;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RequestHeaderFilter extends OncePerRequestFilter {
  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    RequestContext ctx = new RequestContext();
    RequestContextHolder.set(ctx);
    String clientMessageId = request.getHeader(HeaderEnum.CLIENT_MESSAGE_ID.getLabel());
    if (CommonUtil.isNullOrEmpty(clientMessageId)) {
      clientMessageId = UUID.randomUUID().toString();
    }
    ctx.setClientMessageId(clientMessageId);
    String clientTimeStr = request.getHeader(HeaderEnum.CLIENT_TIME.getLabel());
    ctx.setClientTime(clientTimeStr);
    ctx.setReceivedTime(System.currentTimeMillis());
    String clientIp = CommonUtil.getRemoteAddress(request);
    ctx.setClientIp(clientIp);
    String path = request.getRequestURI();
    ctx.setPath(path);
    
    ThreadContext.put("clientIp", clientIp);
    ThreadContext.put("clientMessageId", clientMessageId);
    ThreadContext.put("clientTime", clientTimeStr);
    ThreadContext.put("path", path);
    
    filterChain.doFilter(request, response);
  }

}