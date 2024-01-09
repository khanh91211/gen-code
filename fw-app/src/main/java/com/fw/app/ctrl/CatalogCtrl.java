package com.fw.app.ctrl;

import com.fw.app.service.CatalogService;
import com.fw.app.service.SiteService;
import com.fw.common.base.BaseController;
import com.fw.common.util.RequestUtil;
import com.fw.model.dto.base.SuccessResponse;
import com.fw.model.dto.catalog.CatalogDto;
import com.fw.model.dto.catalog.SectorDto;
import com.fw.model.dto.catalog.SiteDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/v1.0/ch/catalog")
@Api(tags = "Catalog Controller")
public class CatalogCtrl extends BaseController {
    @Autowired
    private SiteService siteService;

    @Autowired
    private CatalogService catalogService;

    @ApiOperation(value = "This is get all sector api.")
    @ResponseBody
    @GetMapping(value = "/get-all-sector")
    public SuccessResponse<List<SectorDto>> getAllSector() {
        return RequestUtil.ok(new ArrayList<SectorDto>());
    }

    @ApiOperation("API lấy danh sách tất cả các site")
    @GetMapping(value = "/get-all-site")
    @ResponseBody
    public SuccessResponse<List<SiteDto>> getSiteList() {
        return RequestUtil.ok(siteService.getSiteList());
    }

    @ApiOperation(value = "get size by code")
    @GetMapping(value = "/get-site-by-code/{siteCode}")
    public SuccessResponse<SiteDto> getSiteByCode(@PathVariable("siteCode") String siteCode) {
        return RequestUtil.ok(new SiteDto());
    }

    @ApiOperation("API lấy danh sách tất cả các catalog")
    @GetMapping(value = "/get-all-catalog")
    @ResponseBody
    public SuccessResponse<List<CatalogDto>> getAllCatalog() {
        return RequestUtil.ok(catalogService.getAllCatalog());
    }

    @ApiOperation(value = "get catalog by line and type")
    @GetMapping(value = "/filter-catalogs")
    public SuccessResponse<List<CatalogDto>> filterCatalogs(@RequestParam("line") String line, @RequestParam("type") String type, @RequestParam("code") String code) {
        return RequestUtil.ok(catalogService.filterCatalogs(line, type, code));
    }

    @ApiOperation(value = "get catalog by line and type")
    @GetMapping(value = "/get-catalogs-by-parent-id")
    public SuccessResponse<List<CatalogDto>> getCatalogsByParentId(@RequestParam("id") long id) {
        return RequestUtil.ok(catalogService.getCatalogsByParentId(id));
    }
}
