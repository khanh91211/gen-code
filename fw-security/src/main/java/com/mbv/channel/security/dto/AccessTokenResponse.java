package com.fw.channel.security.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class AccessTokenResponse {
    private String access_token;
    private long expires_in;
    private long refresh_expires_in;
    private String refresh_token;
    private String token_type;
    private String session_state;
    private String scope;
}
