package com.fw.channel.catalog.service.impl;

import com.fw.channel.catalog.repo.CatalogRepository;
import com.fw.channel.catalog.service.CatalogService;
import com.fw.core.config.adapter.RedisSentinelAdapter;
import com.fw.core.util.CommonUtil;
import com.fw.model.Constants;
import com.fw.model.dto.catalog.CatalogDto;
import com.fw.model.enumeration.ConfigTypeEnum;
import com.fw.model.enumeration.CrdTypeEnum;
import com.fw.model.enumeration.LineEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class CatalogServiceImpl implements CatalogService {
    @Autowired
    private RedisSentinelAdapter redisAdapter;
    @Autowired
    private CatalogRepository catalogRepository;

    @Override
    @Transactional(readOnly = true)
    public List<CatalogDto> getAllCatalog() {
        List<?> listJson = redisAdapter.get("REDIS_ALL_CATALOG", List.class);
        if (!CommonUtil.listIsEmptyOrNull(listJson)) {
            return CommonUtil.mapAsList(listJson);
        } else {
            List<CatalogDto> listObject = CommonUtil.mapAsList(catalogRepository.findAll());
            redisAdapter.set("REDIS_ALL_CATALOG", 86400, listObject);
            return listObject;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<CatalogDto> filterCatalogs(String line, String type, String code) {
        if ((line.equals(LineEnum.VHTD.getLine()) && (type.equals(CrdTypeEnum.VHTD_TYPE_1.getType()) || type.equals(CrdTypeEnum.VHTD_TYPE_9.getType()))) ||
            (line.equals(LineEnum.TT_TTTM.getLine()) && (type.equals(ConfigTypeEnum.TFT_TRANS_COEFF.getType())))
        ) {
            switch (code) {
                case Constants.TRANS_TYPE_CODE:
                    return CommonUtil.mapAsList(catalogRepository.findCatalogWithType());
                case Constants.TRANS_DETAIL_CODE:
                    return CommonUtil.mapAsList(catalogRepository.findCatalogWithDetail());
                case Constants.TTTM_TRANS_CHILD_CODE:
                    return CommonUtil.mapAsList(catalogRepository.findOnOfLineTTTM());
                default:
                    return new ArrayList<>();
            }
        } else if (CommonUtil.isNullOrEmpty(line) || CommonUtil.isNullOrEmpty(type)) {
            List<?> listJson = redisAdapter.get("REDIS_ALL_CATALOG", List.class);
            if (!CommonUtil.listIsEmptyOrNull(listJson)) {
                return CommonUtil.mapAsList(listJson);
            } else {
                List<CatalogDto> listObject = CommonUtil.mapAsList(catalogRepository.findAll());
                redisAdapter.set("REDIS_ALL_CATALOG", 86400, listObject);
                return listObject;
            }
        } else if (CommonUtil.isNullOrEmpty(code)) {
            String key = "REDIS_CATALOG_line_" + line + "_type_" + type;
            List<?> listJson = redisAdapter.get(key, List.class);
            if (!CommonUtil.listIsEmptyOrNull(listJson)) {
                return CommonUtil.mapAsList(listJson);
            } else {
                List<CatalogDto> lstCatalog = CommonUtil.mapAsList(catalogRepository.findByLineAndType(line, type));
                redisAdapter.set(key, 86400, lstCatalog);
                return lstCatalog;
            }
        } else {
            String key = "REDIS_CATALOG_line_" + line + "_type_" + type + "_code_" + code;
            List<?> listJson = redisAdapter.get(key, List.class);
            if (!CommonUtil.listIsEmptyOrNull(listJson)) {
                return CommonUtil.mapAsList(listJson);
            } else {
                List<CatalogDto> lstCatalog = CommonUtil.mapAsList(catalogRepository.findByLineAndTypeAndCode(line, type, code));
                redisAdapter.set(key, 86400, lstCatalog);
                return lstCatalog;
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<CatalogDto> getCatalogsByParentId(long id) {
        String key = "REDIS_CATALOG_id_" + id;
        List<?> listJson = redisAdapter.get(key, List.class);
        if (!CommonUtil.listIsEmptyOrNull(listJson)) {
            return CommonUtil.mapAsList(listJson);
        } else {
            List<CatalogDto> lstCatalog = CommonUtil.mapAsList(catalogRepository.findByParentid(id));
            redisAdapter.set(key, 86400, lstCatalog);
            return lstCatalog;
        }
    }
}
