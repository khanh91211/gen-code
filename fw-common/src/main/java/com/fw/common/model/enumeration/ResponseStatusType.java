package com.fw.common.model.enumeration;

public enum ResponseStatusType {
    SUCCESS(0, "Request thành công"),
    FAILED(1, "Request lỗi")
    ;

    private int status;

    private String message;


    ResponseStatusType(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public int getStatus() {
        return this.status;
    }


    public String getMessage() {
        return message;
    }

}