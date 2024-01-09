package com.fw.common.model;

import lombok.Data;

@Data
public class BaseResponse {
    private int status;
    private long duration;
    private String path;
    private String responseTime;
    private String clientTime;
    private String clientMessageId;
}
