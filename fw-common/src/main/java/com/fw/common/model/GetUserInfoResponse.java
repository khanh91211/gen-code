package com.fw.common.model;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class GetUserInfoResponse {
    private String username;
    private String fullName;
    private String siteCode;
    private List<String> rights;
    private List<String> roles;
    private List<String> groups;
    private String userT24;
}
