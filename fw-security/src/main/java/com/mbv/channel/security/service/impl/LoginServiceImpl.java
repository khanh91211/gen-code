package com.fw.channel.security.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fw.channel.security.dto.AccessTokenResponse;
import com.fw.channel.security.dto.GetUserInfoDto;
import com.fw.channel.security.dto.LoginResponse;
import com.fw.channel.security.enums.GrantType;
import com.fw.channel.security.enums.StatusType;
import com.fw.channel.security.properties.KeycloakApiProperty;
import com.fw.channel.security.properties.KeycloakClientProperty;
import com.fw.channel.security.repo.SRightRepository;
import com.fw.channel.security.repo.SRoleRepository;
import com.fw.channel.security.repo.SUserGroupRepository;
import com.fw.channel.security.repo.SUserRepository;
import com.fw.channel.security.service.LoginService;
import com.fw.core.config.adapter.RedisAdapter;
import com.fw.core.integration.CommonCacheService;
import com.fw.core.util.CommonUtil;
import com.fw.model.Constants;
import com.fw.model.dto.base.GetUserInfoResponse;
import com.fw.model.dto.catalog.SiteDto;
import com.fw.model.dto.exception.BusinessException;
import com.fw.model.entity.sys.*;
import com.fw.model.enumeration.ErrorEnum;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @author binhph
 * @version 1.0
 */
public class LoginServiceImpl implements LoginService {
    @Autowired
    private SUserRepository sUserRepository;
    @Autowired
    private SUserGroupRepository sUserGroupRepository;
    @Autowired
    private SRoleRepository sRoleRepository;
    @Autowired
    private SRightRepository sRightRepository;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private RedisAdapter redisAdapter;
    @Autowired
    private CommonCacheService commonCacheService;
    @Autowired
    private KeycloakApiProperty keycloakApiProperty;
    @Autowired
    private KeycloakClientProperty keycloakClientProperty;
    private static final String TOKEN_PREFIX = "Bearer ";
    private static final String EXPIRE_PREFIX = "expireToken::";

    /**
     * Generate token when login or refresh token
     *
     * @param token refresh token
     * @return {@link LoginResponse}
     */
    @Override
    @Transactional(rollbackFor = BusinessException.class)
    public Object generateToken(String token) throws BusinessException {
        String username = null;
        String password = null;
        String userStatus = null;
        String rmCode = null;
        String siteCode = null;
        Long siteId = null;
        LoginResponse loginResponse = new LoginResponse();
        AccessTokenResponse accessTokenResponse = null;
        if (token == null || !token.startsWith("Basic ")) {
            throw new BusinessException(ErrorEnum.CHECK_AUTHEN);
        }
        username = new String(Base64.getDecoder().decode(token.substring(6))).split(":")[0].toLowerCase().trim();
        password = new String(Base64.getDecoder().decode(token.substring(6))).split(":")[1].trim();
        accessTokenResponse = generateToken(username, password);
        if (accessTokenResponse == null) {
            throw new BusinessException(ErrorEnum.USER_NOT_FOUND);
        }
        loginResponse.setRefreshToken(accessTokenResponse.getRefresh_token());

        List<String> scope = List.of(accessTokenResponse.getScope().split(" "));
        loginResponse.setAccessToken(accessTokenResponse.getAccess_token());
        loginResponse.setTimeExpiration(accessTokenResponse.getExpires_in());
//        get user
        SUser user = sUserRepository.getUserByUserName(username);
        userStatus = StatusType.valueOf(user.getStatus()).getValue();
//        get site
        SUserSite userSite = sUserRepository.getUserSiteByUserId(user.getId());
        SiteDto siteDto = commonCacheService.getSiteCode().get(userSite.getSiteId());
        if (siteDto != null) {
            siteId = siteDto.getId();
            siteCode = siteDto.getCode();
        }

        if (siteId == null || siteCode == null) {
            throw new BusinessException(ErrorEnum.USER_NOT_ACCEPT);
        }
        // set info
        long idUser = user.getId();
        loginResponse.setUserName(username);
        loginResponse.setRmCode(rmCode);
        loginResponse.setUserStatus(userStatus);
        loginResponse.setAuthorizes(getRightByUserId(idUser));
        loginResponse.setGroups(getGroupsByUserId(idUser));
        loginResponse.setRoles(getRolesByUserId(idUser));
        loginResponse.setScopes(scope);
        loginResponse.setSiteCode(siteCode);
        loginResponse.setUserT24(user.getUserT24());
        loginResponse.setFullName(user.getFullname());

        cacheUserData(loginResponse, idUser, accessTokenResponse.getAccess_token());

        return loginResponse;
    }

    private void cacheUserData(LoginResponse loginResponse, long userId, String accessToken) {
        GetUserInfoResponse response = new GetUserInfoResponse();
        response.setSiteCode(loginResponse.getSiteCode());
        response.setUsername(loginResponse.getUserName());
        response.setFullName(loginResponse.getFullName());
        response.setUserT24(loginResponse.getUserT24());
        response.setGroups(loginResponse.getGroups());
        response.setRoles(loginResponse.getRoles());
        response.setRights(loginResponse.getAuthorizes());

        if (accessToken != null) {
            int tokenHash = accessToken.hashCode();
            String key = EXPIRE_PREFIX + tokenHash;
            this.redisAdapter.set(key, Constants.REDIS_EXPIRE_DURATION, response);
            String authKey = "authority::" + tokenHash;
            this.redisAdapter.set(authKey, Constants.REDIS_EXPIRE_DURATION, getRightByUserId(userId));
        }
    }

    @Override
    @Transactional(rollbackFor = BusinessException.class)
    public Object refresh(String token, String refreshToken, String username) throws BusinessException {
        if (Strings.isBlank(refreshToken)) {
            throw new BusinessException(ErrorEnum.REFRESH_TOKEN_REQUIRED);
        }
        SUser user = sUserRepository.getUserByUserName(username);
        String userStatus = StatusType.valueOf(user.getStatus()).getValue();
        SUserSite userSite = sUserRepository.getUserSiteByUserId(user.getId());

        String siteCode = null;
        Long siteId = null;
        if (userSite != null) {
            siteId = userSite.getSiteId();
            siteCode = sUserRepository.getSiteById(siteId).getCode();
        }
        if (siteId == null || siteCode == null) {
            throw new BusinessException(ErrorEnum.USER_NOT_ACCEPT);
        }
        String keyOld = EXPIRE_PREFIX + token;
        String authOld = "authority::" + token;
        this.redisAdapter.delete(keyOld);
        this.redisAdapter.delete(authOld);

        AccessTokenResponse accessTokenResponse = refreshToken(refreshToken);
        if (accessTokenResponse == null) {
            throw new BusinessException(ErrorEnum.REFRESH_TOKEN_NOT_EXIST);
        }

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setAccessToken(accessTokenResponse.getAccess_token());
        loginResponse.setTimeExpiration(accessTokenResponse.getExpires_in());

        long idUser = user.getId();
        loginResponse.setSiteCode(siteCode);
        loginResponse.setUserName(username);
        loginResponse.setFullName(user.getFullname());
        loginResponse.setUserT24(user.getUserT24());
        loginResponse.setGroups(getGroupsByUserId(idUser));
        loginResponse.setRoles(getRolesByUserId(idUser));
        loginResponse.setUserStatus(userStatus);
        loginResponse.setAuthorizes(getRightByUserId(idUser));
        loginResponse.setScopes(List.of(accessTokenResponse.getScope().split(" ")));
        loginResponse.setRefreshToken(accessTokenResponse.getRefresh_token());

        cacheUserData(loginResponse, idUser, accessTokenResponse.getAccess_token());

        return loginResponse;
    }

    @Override
    @Transactional
    public void revoke(String refreshToken, String authorization) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("client_id", keycloakClientProperty.getId());
        map.add("client_secret", keycloakClientProperty.getSecret());
        map.add("refresh_token", refreshToken);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, null);
        restTemplate.postForObject(keycloakApiProperty.getLogout(), request, String.class);
        if (authorization != null && authorization.startsWith(TOKEN_PREFIX)) {
            String accessToken = authorization.substring(TOKEN_PREFIX.length());
            String key = EXPIRE_PREFIX + accessToken.hashCode();
            this.redisAdapter.delete(key);
        }
    }

    /**
     * Get current user login info
     *
     * @return Object
     */
    @Override
    public SUser getUserInfo(String accessToken) throws BusinessException {
        ResponseEntity<GetUserInfoDto> entity = this.userInfo(accessToken);
        if (entity.getStatusCode().is4xxClientError()) {
            throw new BusinessException(ErrorEnum.CHECK_AUTHEN);
        }
        GetUserInfoDto dto = entity.getBody();
        return sUserRepository.getUserByUserName(dto != null ? dto.getPreferred_username() : null);
    }

    private AccessTokenResponse generateToken(String username, String password) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("username", Collections.singletonList(username));
        map.put("password", Collections.singletonList(password));
        map.put("grant_type", Collections.singletonList("password"));
        HttpHeaders headers = new HttpHeaders();

        String auth = keycloakClientProperty.getId() + ":" + keycloakClientProperty.getSecret();
        byte[] authBytes = auth.getBytes(Charset.forName(StandardCharsets.US_ASCII.toString()));
        byte[] encodedAuth = Base64.getEncoder().encode(authBytes);
        String authHeader = "Basic " + new String(encodedAuth);

        headers.add("Authorization", authHeader);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);
        ResponseEntity<AccessTokenResponse> response = this.restTemplate.postForEntity(this.keycloakApiProperty.getToken(), entity,
                AccessTokenResponse.class);
        return response.getBody();
    }

    private AccessTokenResponse refreshToken(String refreshToken) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("client_id", Collections.singletonList(this.keycloakClientProperty.getId()));
        map.put("refresh_token", Collections.singletonList(refreshToken));
        map.put("grant_type", Collections.singletonList(GrantType.refresh_token.toString()));
        map.put("client_secret", Collections.singletonList(this.keycloakClientProperty.getSecret()));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);
        ResponseEntity<AccessTokenResponse> response = this.restTemplate.postForEntity(this.keycloakApiProperty.getToken(), entity,
                AccessTokenResponse.class);
        return response.getBody();
    }

    private ResponseEntity<GetUserInfoDto> userInfo(String token) {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Authorization", token);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(null, headers);
        return restTemplate.postForEntity(keycloakApiProperty.getUserinfo(), request, GetUserInfoDto.class);
    }

    private List<String> getGroupsByUserId(long id) {
        String keyGroup = "groupsUser::" + id;
        String groupStr = redisAdapter.get(keyGroup, String.class);
        if (groupStr != null) {
            return CommonUtil.convertStringToObject(groupStr, new TypeReference<ArrayList<String>>() {
            });
        }

        Set<String> groupSet = new HashSet<>();
        List<SGroup> sGroups = sUserGroupRepository.getGroupsByUserId(id);
        for (SGroup sGroup : sGroups) {
            groupSet.add(sGroup.getName());
        }

        List<String> groupList = new ArrayList<>(groupSet);
        this.redisAdapter.set(keyGroup, Constants.REDIS_EXPIRE_DURATION, groupList);
        return groupList;
    }

    private List<String> getRolesByUserId(long id) {
        String key = "rolesUser::" + id;
        String roleStr = redisAdapter.get(key, String.class);
        if (roleStr != null) {
            return CommonUtil.convertStringToObject(roleStr, new TypeReference<ArrayList<String>>() {
            });
        }
        Set<String> roleSet = new HashSet<>();
        List<SRole> sRoleLs = sRoleRepository.getRoleByUserId(id);
        for (SRole sRole : sRoleLs) {
            roleSet.add(sRole.getName());
        }
        List<String> roleList = new ArrayList<>(roleSet);
        this.redisAdapter.set(key, Constants.REDIS_EXPIRE_DURATION, roleList);
        return roleList;
    }

    private List<String> getRightByUserId(long id) {
        String key = "rightsUser::" + id;
        Set<String> rightSet = new HashSet<>();
        List<SRight> sRightLs = sRightRepository.getRightByUserId(id);
        for (SRight sRight : sRightLs) {
            rightSet.add(sRight.getName());
        }
        List<String> rightList = new ArrayList<>(rightSet);
        this.redisAdapter.set(key, Constants.REDIS_EXPIRE_DURATION, rightList);
        return rightList;
    }
}
