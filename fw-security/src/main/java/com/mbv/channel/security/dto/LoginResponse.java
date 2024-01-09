package com.fw.channel.security.dto;

import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Dto response when login success
 *
 * @author haidv
 * @version 1.0
 */
@Getter
@Setter
public class LoginResponse {
	private String accessToken;
	private String accessTokenOtp;
	private Long timeExpiration;
	private String refreshToken;
	private String userName;
	private String siteCode;
	private String rmCode;
	private String userStatus;
	private String otpUser;
	private String otpSystem;
	private String emailOther;
	private String emailMB;
	private String userT24;
	private String fullName;
	private List<String> scopes;
	private List<String> roles;
	private List<String> authorizes;
	private List<String> groups;
	private GetUserInfoRepsonse info;
	// @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+7")
	private Date serverTime = new Date();
}
