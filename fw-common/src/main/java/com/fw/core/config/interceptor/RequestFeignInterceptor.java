package com.fw.core.config.interceptor;

import com.fw.core.util.RequestContextHolder;
import com.fw.core.util.CredentialsManager;
import com.fw.model.context.RequestContext;
import com.fw.model.enumeration.HeaderEnum;
import feign.RequestInterceptor;
import feign.RequestTemplate;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class RequestFeignInterceptor implements RequestInterceptor {

  private SimpleDateFormat sdf;
  private CredentialsManager manager;
  private Set<String> ignoreAuthorizationSet;

  public RequestFeignInterceptor(SimpleDateFormat sdf, CredentialsManager manager) {
    this.manager = manager;
    this.sdf = sdf;
    ignoreAuthorizationSet = new HashSet<>();
  }

  @Override
  public void apply(RequestTemplate template) {
    Date requestTime = new Date();
    String url = template.url();
    RequestContext ctx = RequestContextHolder.get();
    if (ctx == null) {
      return;
    }
    template.header(HeaderEnum.CLIENT_MESSAGE_ID.getLabel(), ctx.getClientMessageId());
    template.header(HeaderEnum.CLIENT_TIME.getLabel(), sdf.format(requestTime));
    if (!ignoreAuthorizationSet.contains(url)) {
      template.header("Authorization", "Bearer " + manager.getAccessToken());
    }
    template.header("clientMessageId", ctx.getClientMessageId());
  }
}
