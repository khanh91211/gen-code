package com.fw.channel.catalog.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fw.channel.catalog.repo.SSiteRepository;
import com.fw.channel.catalog.service.SiteService;
import com.fw.core.config.adapter.RedisAdapter;
import com.fw.model.dto.catalog.SiteDto;
import com.fw.core.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SiteServiceImpl implements SiteService {
    @Autowired
    private RedisAdapter redisAdapter;
    @Autowired
    private SSiteRepository sSiteRepository;

    @Override
    @Transactional(readOnly = true)
    public List<SiteDto> getSiteList() {
        List<?> listJson = redisAdapter.get("REDIS_ALL_SITE", List.class);
        if (!CommonUtil.listIsEmptyOrNull(listJson)) {
            return CommonUtil.mapAsList(listJson);
        } else {
            List<SiteDto> listObject = CommonUtil.mapAsList(sSiteRepository.getSiteList());
            redisAdapter.set("REDIS_ALL_SITE", 86400, listObject);
            return listObject;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public SiteDto getSiteByCode(String siteCode) {
        String key = "REDIS_" + siteCode;
        String json = redisAdapter.get(key, String.class);
        if (CommonUtil.isNullOrEmpty(json)) {
            return CommonUtil.convertStringToObject(json, new TypeReference<SiteDto>() {
            });
        } else {
            SiteDto siteDto = CommonUtil.toObject(sSiteRepository.getByCode(siteCode), SiteDto.class);
            redisAdapter.set(key, 86400, siteDto);
            return siteDto;
        }
    }
}
