package com.fw.channel.security.dto;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Setter
@Getter
@NoArgsConstructor
public class GetUserInfoDto {
    private String sub;
    private String upn;
    private boolean email_verify;
    private String name;
    private List<String> groups;
    private String preferred_username;
    private String given_name;
    private String family_name;
    private String email;
}
