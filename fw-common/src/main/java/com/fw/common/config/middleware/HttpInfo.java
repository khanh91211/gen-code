package com.fw.common.config.middleware;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class HttpInfo {
    private String url;
    private Map<String, String> requestHeaders;
    private String requestBody;
    private  Map<String, String> responseHeaders;
    private String responseBody;
    private int status;
    private String requestId;
    private String authorization;
    private long durationTime;
    private String username;
    private Long processId;
}