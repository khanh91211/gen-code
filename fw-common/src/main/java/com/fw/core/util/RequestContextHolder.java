package com.fw.core.util;

import org.springframework.stereotype.Component;

import com.fw.model.context.RequestContext;

@Component
public class RequestContextHolder {
  private RequestContextHolder() {}
  private static final ThreadLocal<RequestContext> holder = new ThreadLocal<>();

  public static void set(RequestContext context) {
    holder.set(context);
  }

  public static RequestContext get() {
    return holder.get();
  }

  public static void remove() {
    holder.remove();
  }
}
