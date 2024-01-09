package com.fw.app.service;

import com.fw.model.dto.catalog.SiteDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SiteService {
    List<SiteDto> getSiteList();
    SiteDto getSiteByCode(String siteCode);
}
