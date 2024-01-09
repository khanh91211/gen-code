package com.fw.channel.security.enums;

public enum StatusType {
    LOCK("I"),
    UNLOCK("A"),
    WAIT_APPROVE("F"),
    DENY("R"),
    I("LOCK"),
    A("UNLOCK"),
    F("WAIT_APPROVE"),
    R("DENY")
    ;

    private final String value;

    StatusType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
