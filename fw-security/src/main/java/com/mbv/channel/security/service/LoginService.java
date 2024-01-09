package com.fw.channel.security.service;

import com.fw.model.dto.exception.BusinessException;
import com.fw.model.entity.sys.SUser;

public interface LoginService {

    Object generateToken(String token)
            throws BusinessException;

    void revoke(String refreshToken, String authorization);

    SUser getUserInfo(String username) throws BusinessException;

    Object refresh(String token,String refresh, String username)
            throws BusinessException;

}
