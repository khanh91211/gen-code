package com.fw.app.service;

import com.fw.model.dto.catalog.CatalogDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CatalogService {
    List<CatalogDto> getAllCatalog();
    List<CatalogDto> filterCatalogs(String line, String type, String code);
    List<CatalogDto> getCatalogsByParentId(long id);
}
