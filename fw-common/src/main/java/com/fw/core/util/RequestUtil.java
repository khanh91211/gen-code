package com.fw.core.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.fw.model.enumeration.ResponseStatusType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fw.model.context.RequestContext;
import com.fw.model.dto.base.BaseResponse;
import com.fw.model.dto.base.ErrorResponse;
import com.fw.model.dto.base.SuccessResponse;

import lombok.NoArgsConstructor;

@Component
@NoArgsConstructor
public class RequestUtil {
  private static SimpleDateFormat sdf;

  @Autowired
  public RequestUtil(SimpleDateFormat sdf) {
    RequestUtil.sdf = sdf;
  }

  public static <T> SuccessResponse<T> ok(T data) {
    SuccessResponse<T> ret = new SuccessResponse<>(data);
    getResponseData(ret, ResponseStatusType.SUCCESS.getStatus());
    return ret;
  }

  public static ErrorResponse err(String errorCode, String errorMessage, Object additionalData) {
    ErrorResponse ret = new ErrorResponse(errorCode, errorMessage, additionalData);
    getResponseData(ret, ResponseStatusType.FAILED.getStatus());
    return ret;
  }

  private static void getResponseData(BaseResponse ret, int status) {
    Date responseTime = new Date();
    RequestContext ctx = RequestContextHolder.get();
    ret.setStatus(status);
    ret.setDuration(responseTime.getTime() - ctx.getReceivedTime());
    ret.setResponseTime(sdf.format(responseTime));
    ret.setClientMessageId(ctx.getClientMessageId());
    ret.setClientTime(ctx.getClientTime());
    ret.setPath(ctx.getPath());
  }
}
