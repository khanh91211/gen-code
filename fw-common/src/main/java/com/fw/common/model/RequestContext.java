package com.fw.common.model;

import lombok.Data;

@Data
public class RequestContext {
  private String clientMessageId;
  private String clientTime;
  private long receivedTime;
  private String path;
  private String clientIp;
  private String clientName;
}
