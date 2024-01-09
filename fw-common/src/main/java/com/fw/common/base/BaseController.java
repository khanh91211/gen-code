package com.fw.common.base;

import com.fw.common.config.adapter.RedisAdapter;
import com.fw.common.model.GetUserInfoResponse;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;

public class BaseController {
    @Autowired
    private HttpServletRequest request;
    private static final String TOKEN_PREFIX = "Bearer ";
    private static final String EXPIRE_TOKEN_PREFIX = "expireToken::";
    private static final String AUTHORIZATION = "Authorization";
    @Autowired
    private RedisAdapter redisAdapter;

    protected String getUsername() {
        String token = request.getHeader(AUTHORIZATION);
        if (token != null && token.startsWith(TOKEN_PREFIX)) {
            String accessToken = token.substring(TOKEN_PREFIX.length());
            String key = EXPIRE_TOKEN_PREFIX + accessToken.hashCode();
            GetUserInfoResponse response = this.redisAdapter.get(key, GetUserInfoResponse.class);
            return response.getUsername();
        }
        return "";
    }

    protected GetUserInfoResponse getUserInfo() {
        String token = request.getHeader(AUTHORIZATION);
        if (token != null && token.startsWith(TOKEN_PREFIX)) {
            String accessToken = token.substring(TOKEN_PREFIX.length());
            String key = EXPIRE_TOKEN_PREFIX + accessToken.hashCode();
            return this.redisAdapter.get(key, GetUserInfoResponse.class);
        }
        return null;
    }

    protected List<String> getGroups() {
        String token = request.getHeader(AUTHORIZATION);
        if (token != null && token.startsWith(TOKEN_PREFIX)) {
            String accessToken = token.substring(TOKEN_PREFIX.length());
            String key = EXPIRE_TOKEN_PREFIX + accessToken.hashCode();
            GetUserInfoResponse response = this.redisAdapter.get(key, GetUserInfoResponse.class);
            return response.getGroups();
        }
        return Collections.emptyList();
    }

    protected List<String> getRoles() {
        String token = request.getHeader(AUTHORIZATION);
        if (token != null && token.startsWith(TOKEN_PREFIX)) {
            String accessToken = token.substring(TOKEN_PREFIX.length());
            String key = EXPIRE_TOKEN_PREFIX + accessToken.hashCode();
            GetUserInfoResponse response = this.redisAdapter.get(key, GetUserInfoResponse.class);
            return response.getRoles();
        }
        return Collections.emptyList();
    }

    protected List<String> getRights() {
        String token = request.getHeader(AUTHORIZATION);
        if (token != null && token.startsWith(TOKEN_PREFIX)) {
            String accessToken = token.substring(TOKEN_PREFIX.length());
            String key = EXPIRE_TOKEN_PREFIX + accessToken.hashCode();
            GetUserInfoResponse response = this.redisAdapter.get(key, GetUserInfoResponse.class);
            return response.getRights();
        }
        return Collections.emptyList();
    }

    protected String getUserT24() {
        String token = request.getHeader(AUTHORIZATION);
        if (token != null && token.startsWith(TOKEN_PREFIX)) {
            String accessToken = token.substring(TOKEN_PREFIX.length());
            String key = EXPIRE_TOKEN_PREFIX + accessToken.hashCode();
            GetUserInfoResponse response = this.redisAdapter.get(key, GetUserInfoResponse.class);
            return response.getUserT24();
        }
        return null;
    }

    protected String getToken() {
        String token = request.getHeader(AUTHORIZATION);
        if (token != null && token.startsWith(TOKEN_PREFIX)) {
            return token;
        }
        return null;
    }

    protected String getSiteCode() {
        String token = request.getHeader(AUTHORIZATION);
        if (token != null && token.startsWith(TOKEN_PREFIX)) {
            String accessToken = token.substring(TOKEN_PREFIX.length());
            String key = EXPIRE_TOKEN_PREFIX + accessToken.hashCode();
            GetUserInfoResponse response = this.redisAdapter.get(key, GetUserInfoResponse.class);
            return response.getSiteCode();
        }
        return "";
    }

}
