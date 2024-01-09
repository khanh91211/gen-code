package com.fw.core.integration;

import com.fw.model.dto.catalog.CatalogDto;
import com.fw.model.dto.catalog.SiteDto;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author khanhtd
 * @Date 2022
 */
@Configuration
@EnableScheduling
@Log4j2
public class CommonCacheService {
    @Autowired
    private ChCatalogClient catalogClient;

    private List<String> lstMethod = new ArrayList<>();
    private static final String MANAGEMENT_UNIT = "MANAGEMENT_UNIT";
    private static final String TITLE = "TITLE";
    private static final String PROD_CODE = "PROD_CODE";
    private static final String TITLE_CODE = "TITLE_CODE";
    private static final String LEVEL_CODE = "LEVEL_CODE";
    private static final String TRANS_TYPE = "TRANS_TYPE";

    /**
     * init Method - lấy danh sách method: public + không phải thư viện
     */
    @PostConstruct
    public void init() {
        Method[] methods = CommonCacheService.class.getDeclaredMethods();
        for (Method method : methods) {
            if (!method.isBridge() && !method.isSynthetic() && Modifier.isPublic(method.getModifiers())) {
                lstMethod.add(method.getName());
            }
        }
    }

    private Map<String, SiteDto> branchMap = new HashMap<>();//thong tin chi nhanh (key la code)
    private Map<Long, SiteDto> siteCode = new HashMap<>();//thong tin chi nhanh (key la id)
    private Map<String, String> streamMap = new HashMap<>();// line 1 TDPD luong
    private Map<String, String> productMap = new HashMap<>();// line 1 TDPD sp cha
    private Map<String, String> subProductMap = new HashMap<>();// line 1 TDPD sp con
    private Map<String, String> planGroupMap = new HashMap<>();// line 1 TDPD nhom phuong an
    private Map<String, String> planTypeMap = new HashMap<>();// line 1 TDPD loai phuong an
    private Map<String, String> approveLevelMap = new HashMap<>();// line 1 TDPD tham quyen phe duyet
    private Map<String, String> areaCodeMap = new HashMap<>();// line 1 TDPD vung
    private Map<String, String> approvalResultMap = new HashMap<>();// line 1 TDPD y  kien phe duyet
    private Map<String, String> approvalFormMap = new HashMap<>();// line 1 TDPD hinh thuc phe duyet
    private Map<String, String> customerVipMap = new HashMap<>();// line 1 TDPD phan khuc
    private Map<String, String> groupMap = new HashMap<>();// line 1 TDPD khoi
    private Map<String, String> planStatusMap = new HashMap<>();// line 1 TDPD trang thai phuong an
    private Map<String, String> subUnit2Map = new HashMap<>();// line 1 TDPD DVTT2
    private Map<String, String> titleMap = new HashMap<>();// line 1 TDPD TITLE
    private Map<String, String> levelMap = new HashMap<>();// line 1 TDPD Level
    private Map<String, Integer> lineNumMap = new HashMap<>();// cache line num
    private Map<String, String> defaultWeightTypeTDPD5Map = new HashMap<>();// default apprConfQualityCoeff
    private Map<String, String> custTypeTDPDMap = new HashMap<>();// line 1 TDPD type all customer type
    private Map<String, String> streamTDPD3Map = new HashMap<>();// line 1 TDPD type ApprConfApprLevelCoeff stream
    private Map<String, String> roleTDPDMap = new HashMap<>();// line 1 TDPD type all role
    private Map<String, String> roleTDPD10Map = new HashMap<>();// line 1 TDPD type ApprConfSlaDuration role
    private Map<String, String> extraApprCoeffTypeMap = new HashMap<>();// line 1 TDPD type ApprConfApprTypeCoeff loai he so phe duyet cong them
    private Map<String, String> mngUnitTDPD6Map = new HashMap<>();// line 1 TDPD type ApprConfMgmtCoeff DVQL
    private Map<String, String> mngUnitTDPD9Map = new HashMap<>();// line 1 TDPD type ApprConfMgmtCoeff DVQL
    private Map<String, String> titleTDPD6Map = new HashMap<>();// line 1 TDPD ApprConfMgmtCoeff chuc danh
    private Map<String, String> titleTDPD7Map = new HashMap<>();// line 1 TDPD ApprConfWorkLimit chuc danh
    private Map<String, String> titleTDPD9Map = new HashMap<>();// line 1 TDPD ApprConfWorkCoeffStd chuc danh
    private Map<String, String> planGroupTDPDMap = new HashMap<>();// line 1 TDPD type all nhom phuong an
    private Map<String, String> planTypeTDPD1Map = new HashMap<>();// line 1 TDPD type ApprConfProcType loai phuong an
    private Map<String, String> weightTypeTDPD4Map = new HashMap<>();// line 1 TDPD type ApprConfQualityWeight loai trong so
    private Map<String, String> weightTypeTDPD5Map = new HashMap<>();// line 1 TDPD type ApprConfQualityWeight loai chi tieu CLDV
    private Map<String, String> limitTypeTDPD7Map = new HashMap<>();// line 1 TDPD type ApprConfWorkLimit loai NSLD can gioi han
    private Map<String, String> grProdCodeKhcn1Map = new HashMap<>(); // line khcn type 1: tên tiêu chí cha
    private Map<String, String> prodCodeKhcn1Map = new HashMap<>(); // line khcn type 1: tên tiêu chí con
    private Map<String, String> prodCodeKhcn2Map = new HashMap<>(); // line khcn type 2: tên tiêu chí
    private Map<String, String> addKhcn2Map = new HashMap<>(); // line khcn type 2: tên tiêu chí con
    private Map<String, String> titleKhcn2Map = new HashMap<>(); // line khcn type 2: tên tiêu chí con
    private Map<String, String> goalCodeKhcn3Map = new HashMap<>();// line khcn type 3 : nợ ấu hoặc quá hạn
    private Map<String, String> grProdCodeKhcn4Map = new HashMap<>(); // line khcn type 4: tên tiêu chí cha
    private Map<String, String> prodCodeKhcn4Map = new HashMap<>();//line khcn type 4: tên tiêu chí con
    private Map<String, String> unitCodeKhcn4Map = new HashMap<>();// line khcn type 4: lấy ra loại đơn vị (Tiền , Hợp đồng)
    private Map<String, String> typeUnitKhcn4Map = new HashMap<>();// line khcn type 4: lấy ra giá trị của loại đơn vị (VND, USD , Hợp đồng)
    private Map<String, String> typeCodeKhcn5Map = new HashMap<>();// line khcn type 5: lấy ra loại đơn vị (Tiền , Hợp đồng)
    private Map<String, String> segmentCodeKhcn5Map = new HashMap<>();//line khcn type 5: lấy ra giá trị của loại đơn vị (VND, USD , Hợp đồng)
    private Map<String, String> titleCodeKhcn6Map = new HashMap<>();// chuc danh
    private Map<String, String> levelCodeKhcn6Map = new HashMap<>();// level cua chuc danh
    private Map<String, String> areaKHDNMap = new HashMap<>();// line 3 KHDN Khu vuc
    private Map<String, String> regionKHDNMap = new HashMap<>();// line 3 KHDN Vung mien
    private Map<String, String> custSegmentMap = new HashMap<>();// line 3 KHDN He so KH
    private Map<String, String> revenueMap = new HashMap<>();// line 3 KHDN Loai doanh thu
    private Map<String, String> titleKHDNMap = new HashMap<>();// line 3 KHDN chuc danh
    private Map<String, String> jobPositionKHDNMap = new HashMap<>();// line 3 KHDN vi tri cong viec
    private Map<String, String> levelKHDNMap = new HashMap<>();// line 3 KHDN level
    private Map<String, String> segmentVhtdMap = new HashMap<>();// line 4 VHTD phân khúc (Group_ID:  CIB, SME, KHCN )
    private Map<String, String> transGroupVhtdMap = new HashMap<>();// line 4 VHTD Nhóm giao dịch
    private Map<String, String> transTypeVhtdMap = new HashMap<>();// line 4 VHTD Loai giao dich
    private Map<String, String> transDtlVhtdMap = new HashMap<>();// line 4 VHTD Chi tiet giao dich
    private Map<String, String> titleVhtdMap = new HashMap<>();// line 4 VHTD Chuc danh
    private Map<String, String> levelVhtdMap = new HashMap<>();// line 4 VHTD Level
    private Map<String, String> productVhtdMap = new HashMap<>();// line 4 VHTD Nhom san pham cha tren BPM
    private Map<String, String> signAddressVhtdMap = new HashMap<>();// line 4 VHTD dia diem in ky
    private Map<String, String> segment2VhtdMap = new HashMap<>();// line 4 VHTD phân khúc (Segment: Vip Normal)
    private Map<String, String> surveyVhtdMap = new HashMap<>();// line 4 VHTD khao sat
    private Map<String, String> unitVhtdMap = new HashMap<>(); // line 4 VHTD Đơn vị quản lý
    private Map<String, String> unit1VhtdMap = new HashMap<>(); // line 4 VHTD ĐVTT1
    private Map<String, String> unit2VhtdMap = new HashMap<>(); // line 4 VHTD ĐVTT2
    private Map<String, String> titleMgmtVhtdMap = new HashMap<>(); // line 4 VHTD Chức danh type 2 hệ số Mgmt
    private Map<String, String> businessTypeVhtdMap = new HashMap<>(); // line 4 VHTD Nghiệp vụ (type 3 va 10)
    // private Map<String, String> typeQualityVhtdMap = new HashMap<>(); // line 4 VHTD Type (SLA, FTR, HLKH)
    private Map<String, String> businessTypeSlaVhtdMap = new HashMap<>(); // line 4 VHTD Nghiệp vụ (Soạn thảo/Giải ngân)
    private Map<String, String> workAddressVhtdMap = new HashMap<>(); // line 4 VHTD Địa điểm làm việc
    private Map<String, String> limitTypeVhtdMap = new HashMap<>(); // line 4 VHTD LimitType (Phân loại trần CVK)
    private Map<String, String> evaluateVhtdMap = new HashMap<>(); // line 4 VHTD evaluate (đánh giá CLDV)
    private Map<String, String> typeSutVhtdMap = new HashMap<>(); // line 4 VHTD typeSut
    private Map<String, String> titleQsMap = new HashMap<>();// line 5 QS chuc danh
    private Map<String, String> levelQsMap = new HashMap<>();// line 5 QS level
    private Map<String, String> transTypeQs1Map = new HashMap<>();// line 5 QS type 1 loai giao dich
    private Map<String, String> transTypeQs2Map = new HashMap<>();// line 5 QS type 2 loai giao dich
    private Map<String, String> sizeQs3Map = new HashMap<>();// line 5 QS size don vi
    private Map<String, String> orgQsMap = new HashMap<>();// line 5 QS loai don vi
    private Map<String, String> hasAuthQs3Map = new HashMap<>();// line 5 QS don vi co KSV
    private Map<String, String> manageScaleQs3Map = new HashMap<>();// line 5 QS quy mo QL
    private Map<String, String> assetScaleQs4Map = new HashMap<>();// line 5 QS quy mo tai san
    private Map<String, String> typeQs7Map = new HashMap<>();// line 5 QS type 7 loai he so
    private Map<String, String> stdTypeQsMap = new HashMap<>();// line 5 QS loai trong/he so CLDV
    private Map<String, String> unitQs10Map = new HashMap<>();// line 5 QS type 10 don vi
    private Map<String, String> prodQs10Map = new HashMap<>();// line 5 QS type 10 san pham bancas
    private Map<String, String> regionQsMap = new HashMap<>();// line 5 QS vung mien

    //Getter
    public Map<String, SiteDto> getBranchMap() {
        return loadRunnableByName(branchMap, this::loadBranches);
    }

    public Map<Long, SiteDto> getSiteCode() {
        return loadRunnableByName(siteCode, this::loadBranches);
    }

    public Map<String, String> getStreamMap() {
        return loadRunnableByName(streamMap, this::loadStreams);
    }

    public Map<String, String> getProductMap() {
        return loadRunnableByName(productMap, this::loadProducts);
    }

    public Map<String, String> getSubProductMap() {
        return loadRunnableByName(subProductMap, this::loadSubProducts);
    }

    public Map<String, String> getPlanGroupMap() {
        return loadRunnableByName(planGroupMap, this::loadPlanGroup);
    }

    public Map<String, String> getPlanTypeMap() {
        return loadRunnableByName(planTypeMap, this::loadPlanType);
    }

    public Map<String, String> getApproveLevelMap() {
        return loadRunnableByName(approveLevelMap, this::loadApproveLevel);
    }

    public Map<String, String> getAreaCodeMap() {
        return loadRunnableByName(areaCodeMap, this::loadAreaCode);
    }

    public Map<String, String> getApprovalResultMap() {
        return loadRunnableByName(approvalResultMap, this::loadApprovalResult);
    }

    public Map<String, String> getApprovalFormMap() {
        return loadRunnableByName(approvalFormMap, this::loadApprovalForm);
    }

    public Map<String, String> getCustomerVipMap() {
        return loadRunnableByName(customerVipMap, this::loadCustomerVip);
    }

    public Map<String, String> getGroupMap() {
        return loadRunnableByName(groupMap, this::loadGroup);
    }

    public Map<String, String> getPlanStatusMap() {
        return loadRunnableByName(planStatusMap, this::loadPlanStatus);
    }

    public Map<String, String> getSubUnit2Map() {
        return loadRunnableByName(subUnit2Map, this::loadSubUnit2);
    }

    public Map<String, String> getTitleMap() {
        return loadRunnableByName(titleMap, this::loadTitle);
    }

    public Map<String, String> getLevelMap() {
        return loadRunnableByName(levelMap, this::loadLevel);
    }

    public Map<String, Integer> getLineNumMap() {
        return loadRunnableByName(lineNumMap, this::loadLineNumMap);
    }

    public Map<String, String> getDefaultWeightTypeTDPD5Map() {
        return loadRunnableByName(defaultWeightTypeTDPD5Map, this::loadDefaultWeightTypeTDPD5Map);
    }

    public Map<String, String> getCustTypeTDPDMap() {
        return loadRunnableByName(custTypeTDPDMap, this::loadCustTypeTDPD);
    }

    public Map<String, String> getStreamTDPD3Map() {
        return loadRunnableByName(streamTDPD3Map, this::loadStreamTDPD3);
    }

    public Map<String, String> getRoleTDPDMap() {
        return loadRunnableByName(roleTDPDMap, this::loadRoleTDPD);
    }

    public Map<String, String> getRoleTDPD10Map() {
        return loadRunnableByName(roleTDPD10Map, this::loadRoleTDPD10);
    }

    public Map<String, String> getExtraApprCoeffTypeMap() {
        return loadRunnableByName(extraApprCoeffTypeMap, this::loadExtraApprCoeffType);
    }

    public Map<String, String> getMngUnitTDPD6Map() {
        return loadRunnableByName(mngUnitTDPD6Map, this::loadMngUnitTDPD6);
    }

    public Map<String, String> getMngUnitTDPD9Map() {
        return loadRunnableByName(mngUnitTDPD9Map, this::loadMngUnitTDPD9);
    }

    public Map<String, String> getTitleTDPD6Map() {
        return loadRunnableByName(titleTDPD6Map, this::loadTitleTDPD6Map);
    }

    public Map<String, String> getTitleTDPD7Map() {
        return loadRunnableByName(titleTDPD7Map, this::loadTitleTDPD7Map);
    }

    public Map<String, String> getTitleTDPD9Map() {
        return loadRunnableByName(titleTDPD9Map, this::loadTitleTDPD7Map);
    }

    public Map<String, String> getPlanGroupTDPDMap() {
        return loadRunnableByName(planGroupTDPDMap, this::loadPlanGroupTDPDMap);
    }

    public Map<String, String> getPlanTypeTDPD1Map() {
        return loadRunnableByName(planTypeTDPD1Map, this::loadPlanTypeTDPD1Map);
    }

    public Map<String, String> getWeightTypeTDPD4Map() {
        return loadRunnableByName(weightTypeTDPD4Map, this::loadWeightTypeTDPD4Map);
    }

    public Map<String, String> getWeightTypeTDPD5Map() {
        return loadRunnableByName(weightTypeTDPD5Map, this::loadWeightTypeTDPD5Map);
    }

    public Map<String, String> getLimitTypeTDPD7Map() {
        return loadRunnableByName(limitTypeTDPD7Map, this::loadLimitTypeTDPD7Map);
    }

    public Map<String, String> getGrProdCodeKhcn1Map() {
        return loadRunnableByName(grProdCodeKhcn1Map, this::loadGrProdCodeKhcn1Map);
    }

    public Map<String, String> getProdCodeKhcn1Map() {
        return loadRunnableByName(prodCodeKhcn1Map, this::loadProdCodeKhcn1Map);
    }

    public Map<String, String> getProdCodeKhcn2Map() {
        return loadRunnableByName(prodCodeKhcn2Map, this::loadProdCodeKhcn2Map);
    }

    public Map<String, String> getAddKhcn2Map() {
        return loadRunnableByName(addKhcn2Map, this:: loadAddKhcn2Map);
    }

    public Map<String, String> getTitleKhcn2Map() {
        return loadRunnableByName(titleKhcn2Map, this::loadTitleKhcn2Map);
    }

    public Map<String, String> getGoalCodeKhcn3Map() {
        return loadRunnableByName(goalCodeKhcn3Map, this::loadGoalCodeKhcn3Map);
    }

    public Map<String, String> getGrProdCodeKhcn4Map() {
        return loadRunnableByName(grProdCodeKhcn4Map, this::loadGrProdCodeKhcn4Map);
    }

    public Map<String, String> getProdCodeKhcn4Map() {
        return loadRunnableByName(prodCodeKhcn4Map, this::loadProdCodeKhcn4Map);
    }

    public Map<String, String> getUnitCodeKhcn4Map() {
        return loadRunnableByName(unitCodeKhcn4Map, this::loadUnitCodeKhcn4Map);
    }

    public Map<String, String> getTypeUnitKhcn4Map() {
        return loadRunnableByName(typeUnitKhcn4Map, this::loadTypeUnitKhcn4Map);
    }

    public Map<String, String> getTypeCodeKhcn5Map() {
        return loadRunnableByName(typeCodeKhcn5Map, this::loadTypeCodeKhcn5Map);
    }

    public Map<String, String> getSegmentCodeKhcn5Map() {
        return loadRunnableByName(segmentCodeKhcn5Map, this::loadSegmentCodeKhcn5Map);
    }

    public Map<String, String> getTitleCodeKhcn6Map() {
        return loadRunnableByName(titleCodeKhcn6Map, this::loadTitleCodeKhcn6Map);
    }

    public Map<String, String> getLevelCodeKhcn6Map() {
        return loadRunnableByName(levelCodeKhcn6Map, this::loadLevelCodeKhcn6Map);
    }

    public Map<String, String> getAreaKHDNMap() {
        return loadRunnableByName(areaKHDNMap, this::loadAreaKHDN);
    }

    public Map<String, String> getRegionKHDNMap() {
        return loadRunnableByName(regionKHDNMap, this::loadRegionKHDN);
    }

    public Map<String, String> getCustSegmentMap() {
        return loadRunnableByName(custSegmentMap, this::loadCustSegment);
    }

    public Map<String, String> getRevenueMap() {
        return loadRunnableByName(revenueMap, this::loadRevenue);
    }

    public Map<String, String> getTitleKHDNMap() {
        return loadRunnableByName(titleKHDNMap, this::loadTitleKHDN);
    }

    public Map<String, String> getJobPositionKHDNMap() {
        return loadRunnableByName(jobPositionKHDNMap, this::loadJobPositionKHDN);
    }

    public Map<String, String> getLevelKHDNMap() {
        return loadRunnableByName(levelKHDNMap, this::loadLevelKHDN);
    }

    public Map<String, String> getSegmentVhtdMap() {
        return loadRunnableByName(segmentVhtdMap, this::loadSegmentVHTD);
    }

    public Map<String, String> getTransGroupVhtdMap() {
        return loadRunnableByName(transGroupVhtdMap, this::loadTransGroupVHTD);
    }

    public Map<String, String> getTransTypeVhtdMap() {
        return loadRunnableByName(transTypeVhtdMap, this::loadTransTypeVHTD);
    }

    public Map<String, String> getTransDtlVhtdMap() {
        return loadRunnableByName(transDtlVhtdMap, this::loadTransDtlVHTD);
    }

    public Map<String, String> getTitleVhtdMap() {
        return loadRunnableByName(titleVhtdMap, this::loadTitleVHTD);
    }

    public Map<String, String> getLevelVhtdMap() {
        return loadRunnableByName(levelVhtdMap, this::loadLevelVHTD);
    }

    public Map<String, String> getProductVhtdMap() {
        return loadRunnableByName(productVhtdMap, this::loadProductVHTD);
    }

    public Map<String, String> getSignAddressVhtdMap() {
        return loadRunnableByName(signAddressVhtdMap, this::loadSignAddressVHTD);
    }

    public Map<String, String> getSegment2VhtdMap() {
        return loadRunnableByName(segment2VhtdMap, this::loadSegment2VHTD);
    }

    public Map<String, String> getSurveyKHDNMap() {
        return loadRunnableByName(surveyVhtdMap, this::loadSurveyVHTD);
    }

    public Map<String, String> getUnitKHDNMap() {
        return loadRunnableByName(unitVhtdMap, this::loadUnitVHTD);
    }

    public Map<String, String> getUnit1KHDNMap() {
        return loadRunnableByName(unit1VhtdMap, this::loadUnit1VHTD);
    }

    public Map<String, String> getUnit2KHDNMap() {
        return loadRunnableByName(unit2VhtdMap, this::loadUnit2VHTD);
    }

    public Map<String, String> getTitleMgmtKHDNMap() {
        return loadRunnableByName(titleMgmtVhtdMap, this::loadTitleMgmtVHTD);
    }

    public Map<String, String> getBusinessTypeKHDNMap() {
        return loadRunnableByName(businessTypeVhtdMap, this::loadBusinessTypeVHTD);
    }

    public Map<String, String> getBusinessTypeSlaKHDNMap() {
        return loadRunnableByName(businessTypeSlaVhtdMap, this::loadBusinessTypeSlaVHTD);
    }

    public Map<String, String> getWorkAddressVhtdMap() {
        return loadRunnableByName(workAddressVhtdMap, this::loadWorkAddressVHTD);
    }

    public Map<String, String> getLimitTypeVhtdMap() {
        return loadRunnableByName(limitTypeVhtdMap, this::loadLimitTypeVHTD);
    }

    public Map<String, String> getEvaluateVhtdMap() {
        return loadRunnableByName(evaluateVhtdMap, this::loadEvaluateVHTD);
    }

    public Map<String, String> getTypeSutVhtdMap() {
        return loadRunnableByName(typeSutVhtdMap, this::loadTypeSutVHTD);
    }

    public Map<String, String> getTitleQsMap() {
        return loadRunnableByName(titleQsMap, this::loadTitleQsMap);
    }

    public Map<String, String> getLevelQsMap() {
        return loadRunnableByName(levelQsMap, this::loadLevelQsMap);
    }

    public Map<String, String> getTransTypeQs1Map() {
        return loadRunnableByName(transTypeQs1Map, this::loadTransTypeQs1Map);
    }

    public Map<String, String> getTransTypeQs2Map() {
        return loadRunnableByName(transTypeQs2Map, this::loadTransTypeQs2Map);
    }

    public Map<String, String> getSizeQs3Map() {
        return loadRunnableByName(sizeQs3Map, this::loadSizeQs3Map);
    }

    public Map<String, String> getOrgQsMap() {
        return loadRunnableByName(orgQsMap, this::loadOrgQsMap);
    }

    public Map<String, String> getHasAuthQs3Map() {
        return loadRunnableByName(hasAuthQs3Map, this::loadHasAuthQs3Map);
    }

    public Map<String, String> getManageScaleQs3Map() {
        return loadRunnableByName(manageScaleQs3Map, this::loadManageScaleQs3Map);
    }

    public Map<String, String> getAssetScaleQs4Map() {
        return loadRunnableByName(assetScaleQs4Map, this::loadAssetScaleQs4Map);
    }

    public Map<String, String> getTypeQs7Map() {
        return loadRunnableByName(typeQs7Map, this::loadTypeQs7Map);
    }

    public Map<String, String> getStdTypeQsMap() {
        return loadRunnableByName(stdTypeQsMap, this::loadStdTypeQsMap);
    }

    public Map<String, String> getUnitQs10Map() {
        return loadRunnableByName(unitQs10Map, this::loadUnitQs10Map);
    }

    public Map<String, String> getProdQs10Map() {
        return loadRunnableByName(prodQs10Map, this::loadProdQs10Map);
    }

    public Map<String, String> getRegionQsMap() {
        return loadRunnableByName(regionQsMap, this::loadRegionQsMap);
    }

    private void loadBranches() {
        branchMap.clear();
        List<SiteDto> branches = catalogClient.getSiteList().getData();
        if (!CollectionUtils.isEmpty(branches)) {
            branches.forEach(item -> {
                branchMap.put(item.getCode(), item);
                siteCode.put(item.getId(), item);
            });
        }
    }

    private void loadStreams() {
        List<CatalogDto> streams = catalogClient.filterCatalogs("TDPD", "1", "STREAM").getData();
        if (!CollectionUtils.isEmpty(streams)) {
            streamMap.clear();
            streams.forEach(item -> streamMap.put(item.getLabelCode(), item.getLabel()));
        }
    }

    private void loadProducts() {
        List<CatalogDto> products = catalogClient.filterCatalogs("TDPD", "1", "FATHER_PRODUCT").getData();
        if (!CollectionUtils.isEmpty(products)) {
            productMap.clear();
            products.forEach(item -> productMap.put(item.getLabelCode(), item.getLabel()));
        }
    }

    private void loadSubProducts() {
        List<CatalogDto> subProducts = catalogClient.filterCatalogs("TDPD", "1", "CHILD_PRODUCT").getData();
        if (!CollectionUtils.isEmpty(subProducts)) {
            subProductMap.clear();
            subProducts.forEach(item -> subProductMap.put(item.getLabelCode(), item.getLabel()));
        }
    }

    private void loadPlanGroup() {
        List<CatalogDto> planGroups = catalogClient.filterCatalogs("TDPD", "ALL", "PLAN_GROUP").getData();
        if (!CollectionUtils.isEmpty(planGroups)) {
            planGroupMap.clear();
            planGroups.forEach(item -> planGroupMap.put(item.getLabelCode(), item.getLabel()));
        }
    }

    private void loadPlanType() {
        List<CatalogDto> planTypes = catalogClient.filterCatalogs("TDPD", "ALL", "PLAN_TYPE").getData();
        if (!CollectionUtils.isEmpty(planTypes)) {
            planTypeMap.clear();
            planTypes.forEach(item -> planTypeMap.put(item.getLabelCode(), item.getLabel()));
        }
    }

    private void loadApproveLevel() {
        List<CatalogDto> approveLevels = catalogClient.filterCatalogs("TDPD", "3", "APPROVE_LEVEL").getData();
        if (!CollectionUtils.isEmpty(approveLevels)) {
            approveLevelMap.clear();
            approveLevels.forEach(item -> approveLevelMap.put(item.getLabelCode(), item.getLabel()));
        }
    }

    private void loadAreaCode() {
        List<CatalogDto> areaCodes = catalogClient.filterCatalogs("TDPD", "ALL", "AREA_CODE").getData();
        if (!CollectionUtils.isEmpty(areaCodes)) {
            areaCodeMap.clear();
            areaCodes.forEach(item -> areaCodeMap.put(item.getLabelCode(), item.getLabel()));
        }
    }

    private void loadApprovalResult() {
        List<CatalogDto> approvalResults = catalogClient.filterCatalogs("TDPD", "ALL", "APPR_COMMENT").getData();
        if (!CollectionUtils.isEmpty(approvalResults)) {
            approvalResultMap.clear();
            approvalResults.forEach(item -> approvalResultMap.put(item.getLabelCode(), item.getLabel()));
        }
    }

    private void loadApprovalForm() {
        List<CatalogDto> approvalForms = catalogClient.filterCatalogs("TDPD", "ALL", "APPROVAL_FORM").getData();
        if (!CollectionUtils.isEmpty(approvalForms)) {
            approvalFormMap.clear();
            approvalForms.forEach(item -> approvalFormMap.put(item.getLabelCode(), item.getLabel()));
        }
    }

    private void loadGroup() {
        List<CatalogDto> groups = catalogClient.filterCatalogs("TDPD", "ALL", "GROUP").getData();
        if (!CollectionUtils.isEmpty(groups)) {
            groupMap.clear();
            groups.forEach(item -> groupMap.put(item.getLabelCode(), item.getLabel()));
        }
    }

    private void loadCustomerVip() {
        List<CatalogDto> customerVips = catalogClient.filterCatalogs("TDPD", "ALL", "CUSTOMER_VIP").getData();
        if (!CollectionUtils.isEmpty(customerVips)) {
            customerVipMap.clear();
            customerVips.forEach(item -> customerVipMap.put(item.getLabelCode(), item.getLabel()));
        }
    }

    private void loadPlanStatus() {
        List<CatalogDto> planStatus = catalogClient.filterCatalogs("TDPD", "ALL", "PLAN_STATUS").getData();
        if (!CollectionUtils.isEmpty(planStatus)) {
            planStatusMap.clear();
            planStatus.forEach(item -> planStatusMap.put(item.getLabelCode(), item.getLabel()));
        }
    }

    private void loadSubUnit2() {
        List<CatalogDto> subUnit2s = catalogClient.filterCatalogs("TDPD", "9", MANAGEMENT_UNIT).getData();
        if (!CollectionUtils.isEmpty(subUnit2s)) {
            subUnit2Map.clear();
            subUnit2s.forEach(item -> subUnit2Map.put(item.getLabelCode(), item.getLabel()));
        }
    }

    private void loadTitle() {
        List<CatalogDto> lstTitle = catalogClient.filterCatalogs("TDPD", "4", TITLE).getData();
        if (!CollectionUtils.isEmpty(lstTitle)) {
            titleMap.clear();
            lstTitle.forEach(item -> titleMap.put(item.getLabelCode(), item.getLabel()));
        }
    }

    private void loadLevel() {
        List<CatalogDto> lstLevel = catalogClient.filterCatalogs("TDPD", "9", "LEVEL").getData();
        if (!CollectionUtils.isEmpty(lstLevel)) {
            levelMap.clear();
            lstLevel.forEach(item -> levelMap.put(item.getLabelCode(), item.getLabel()));
        }
    }

    private void loadLineNumMap() {
        lineNumMap.clear();
        lineNumMap.put("TDPD", 1);
        lineNumMap.put("KHCN", 2);
        lineNumMap.put("KHDN", 3);
        lineNumMap.put("VHTD", 4);
        lineNumMap.put("QS", 5);
        lineNumMap.put("TTTT", 6);
    }

    private void loadDefaultWeightTypeTDPD5Map() {
        defaultWeightTypeTDPD5Map.clear();
        defaultWeightTypeTDPD5Map.put("COEFF_DEFAULT_SLA", "COEFF_SLA");
        defaultWeightTypeTDPD5Map.put("COEFF_DEFAULT_ACTIVE", "COEFF_ACTIVE");
        defaultWeightTypeTDPD5Map.put("COEFF_DEFAULT_NPL", "COEFF_NPL");
    }

    private void loadCustTypeTDPD() {
        List<CatalogDto> lstCustype = catalogClient.filterCatalogs("TDPD", "ALL", "CUSTOMER_TYPE").getData();
        if (!CollectionUtils.isEmpty(lstCustype)) {
            custTypeTDPDMap.clear();
            lstCustype.forEach(item -> custTypeTDPDMap.put(item.getLabelCode(), item.getLabel()));
        }
    }

    private void loadStreamTDPD3() {
        List<CatalogDto> lstStream = catalogClient.filterCatalogs("TDPD", "3", "STREAM").getData();
        if (!CollectionUtils.isEmpty(lstStream)) {
            streamTDPD3Map.clear();
            lstStream.forEach(item -> streamTDPD3Map.put(item.getLabelCode(), item.getLabel()));
        }
    }

    private void loadRoleTDPD() {
        List<CatalogDto> lstRole = catalogClient.filterCatalogs("TDPD", "ALL", "ROLE").getData();
        if (!CollectionUtils.isEmpty(lstRole)) {
            roleTDPDMap.clear();
            lstRole.forEach(item -> roleTDPDMap.put(item.getLabelCode(), item.getLabel()));
        }
    }

    private void loadRoleTDPD10() {
        List<CatalogDto> lstRole = catalogClient.filterCatalogs("TDPD", "10", "ROLE").getData();
        if (!CollectionUtils.isEmpty(lstRole)) {
            roleTDPD10Map.clear();
            lstRole.forEach(item -> roleTDPD10Map.put(item.getLabelCode(), item.getLabel()));
        }
    }

    private void loadExtraApprCoeffType() {
        List<CatalogDto> lstExtraApprCoeffType = catalogClient.filterCatalogs("TDPD", "8", "EXTRA_APPROVAL_COEFFICIENT_TYPE").getData();
        if (!CollectionUtils.isEmpty(lstExtraApprCoeffType)) {
            extraApprCoeffTypeMap.clear();
            lstExtraApprCoeffType.forEach(item -> extraApprCoeffTypeMap.put(item.getLabelCode(), item.getLabel()));
        }
    }

    private void loadMngUnitTDPD6() {
        List<CatalogDto> lstMngUnit = catalogClient.filterCatalogs("TDPD", "6", MANAGEMENT_UNIT).getData();
        if (!CollectionUtils.isEmpty(lstMngUnit)) {
            mngUnitTDPD6Map.clear();
            lstMngUnit.forEach(item -> mngUnitTDPD6Map.put(item.getLabelCode(), item.getLabel()));
        }
    }

    private void loadMngUnitTDPD9() {
        List<CatalogDto> lstMngUnit = catalogClient.filterCatalogs("TDPD", "9", MANAGEMENT_UNIT).getData();
        if (!CollectionUtils.isEmpty(lstMngUnit)) {
            mngUnitTDPD9Map.clear();
            lstMngUnit.forEach(item -> mngUnitTDPD9Map.put(item.getLabelCode(), item.getLabel()));
        }
    }

    private void loadTitleTDPD6Map() {
        List<CatalogDto> lstTitle = catalogClient.filterCatalogs("TDPD", "6", TITLE).getData();
        if (!CollectionUtils.isEmpty(lstTitle)) {
            titleTDPD6Map.clear();
            lstTitle.forEach(item -> titleTDPD6Map.put(item.getLabelCode(), item.getLabel()));
        }
    }

    private void loadTitleTDPD7Map() {
        List<CatalogDto> lstTitle = catalogClient.filterCatalogs("TDPD", "7", TITLE).getData();
        if (!CollectionUtils.isEmpty(lstTitle)) {
            titleTDPD7Map.clear();
            lstTitle.forEach(item -> titleTDPD7Map.put(item.getLabelCode(), item.getLabel()));
        }
    }

    private void loadTitleTDPD9Map() {
        List<CatalogDto> lstTitle = catalogClient.filterCatalogs("TDPD", "9", TITLE).getData();
        if (!CollectionUtils.isEmpty(lstTitle)) {
            titleTDPD9Map.clear();
            lstTitle.forEach(item -> titleTDPD9Map.put(item.getLabelCode(), item.getLabel()));
        }
    }

    private void loadPlanGroupTDPDMap() {
        List<CatalogDto> lstPlanGroup = catalogClient.filterCatalogs("TDPD", "ALL", "PLAN_GROUP").getData();
        if (!CollectionUtils.isEmpty(lstPlanGroup)) {
            planGroupTDPDMap.clear();
            lstPlanGroup.forEach(item -> planGroupTDPDMap.put(item.getLabelCode(), item.getLabel()));
        }
    }

    private void loadPlanTypeTDPD1Map() {
        List<CatalogDto> lstPlanType = catalogClient.filterCatalogs("TDPD", "1", "PLAN_TYPE").getData();
        if (!CollectionUtils.isEmpty(lstPlanType)) {
            planTypeTDPD1Map.clear();
            lstPlanType.forEach(item -> planTypeTDPD1Map.put(item.getLabelCode(), item.getLabel()));
        }
    }

    private void loadWeightTypeTDPD4Map() {
        List<CatalogDto> lstWeightType = catalogClient.filterCatalogs("TDPD", "4", "WEIGHT_TYPE").getData();
        if (!CollectionUtils.isEmpty(lstWeightType)) {
            weightTypeTDPD4Map.clear();
            lstWeightType.forEach(item -> weightTypeTDPD4Map.put(item.getLabelCode(), item.getLabel()));
        }
    }

    private void loadWeightTypeTDPD5Map() {
        List<CatalogDto> lstWeightType = catalogClient.filterCatalogs("TDPD", "5", "WEIGHT_TYPE").getData();
        if (!CollectionUtils.isEmpty(lstWeightType)) {
            weightTypeTDPD5Map.clear();
            lstWeightType.forEach(item -> weightTypeTDPD5Map.put(item.getLabelCode(), item.getLabel()));
        }
    }

    private void loadLimitTypeTDPD7Map() {
        List<CatalogDto> lstLimitType = catalogClient.filterCatalogs("TDPD", "7", "LIMIT_TYPE").getData();
        if (!CollectionUtils.isEmpty(lstLimitType)) {
            limitTypeTDPD7Map.clear();
            lstLimitType.forEach(item -> limitTypeTDPD7Map.put(item.getLabelCode(), item.getLabel()));
        }
    }

    private void loadGrProdCodeKhcn1Map() {
        List<CatalogDto> lstLimitType = catalogClient.filterCatalogs("KHCN", "1", "GROUP_PROD_CODE").getData();
        if (!CollectionUtils.isEmpty(lstLimitType)) {
            grProdCodeKhcn1Map.clear();
            lstLimitType.forEach(item -> grProdCodeKhcn1Map.put(item.getLabelCode(), item.getLabel()));
        }
    }

    private void loadProdCodeKhcn1Map() {
        List<CatalogDto> lstLimitType = catalogClient.filterCatalogs("KHCN", "1", PROD_CODE).getData();
        if (!CollectionUtils.isEmpty(lstLimitType)) {
            prodCodeKhcn1Map.clear();
            lstLimitType.forEach(item -> prodCodeKhcn1Map.put(item.getLabelCode(), item.getLabel()));
        }
    }

    private void loadProdCodeKhcn2Map() {
        List<CatalogDto> lstLimitType = catalogClient.filterCatalogs("KHCN", "2", PROD_CODE).getData();
        if (!CollectionUtils.isEmpty(lstLimitType)) {
            prodCodeKhcn2Map.clear();
            lstLimitType.forEach(item -> prodCodeKhcn2Map.put(item.getLabelCode(), item.getLabel()));
        }
    }

    private void loadAddKhcn2Map() {
        List<CatalogDto> lstLimitType = catalogClient.filterCatalogs("KHCN", "2", "ADD_CODE").getData();
        if (!CollectionUtils.isEmpty(lstLimitType)) {
            addKhcn2Map.clear();
            lstLimitType.forEach(item -> addKhcn2Map.put(item.getLabelCode(), item.getLabel()));
        }
    }

    private void loadTitleKhcn2Map() {
        List<CatalogDto> lstLimitType = catalogClient.filterCatalogs("KHCN", "2", TITLE_CODE).getData();
        if (!CollectionUtils.isEmpty(lstLimitType)) {
            titleKhcn2Map.clear();
            lstLimitType.forEach(item -> titleKhcn2Map.put(item.getLabelCode(), item.getLabel()));
        }
    }

    private void loadGoalCodeKhcn3Map() {
        List<CatalogDto> lstLimitType = catalogClient.filterCatalogs("KHCN", "3", "GOAL_CODE").getData();
        if (!CollectionUtils.isEmpty(lstLimitType)) {
            goalCodeKhcn3Map.clear();
            lstLimitType.forEach(item -> goalCodeKhcn3Map.put(item.getLabelCode(), item.getLabel()));
        }
    }

    private void loadGrProdCodeKhcn4Map() {
        List<CatalogDto> lstLimitType = catalogClient.filterCatalogs("KHCN", "4", "GROUP_PROD_CODE").getData();
        if (!CollectionUtils.isEmpty(lstLimitType)) {
            grProdCodeKhcn4Map.clear();
            lstLimitType.forEach(item -> grProdCodeKhcn4Map.put(item.getLabelCode(), item.getLabel()));
        }
    }

    private void loadProdCodeKhcn4Map() {
        List<CatalogDto> lstLimitType = catalogClient.filterCatalogs("KHCN", "4", PROD_CODE).getData();
        if (!CollectionUtils.isEmpty(lstLimitType)) {
            prodCodeKhcn4Map.clear();
            lstLimitType.forEach(item -> prodCodeKhcn4Map.put(item.getLabelCode(), item.getLabel()));
        }
    }

    private void loadUnitCodeKhcn4Map() {
        List<CatalogDto> lstLimitType = catalogClient.filterCatalogs("KHCN", "4", "UNIT_CODE").getData();
        if (!CollectionUtils.isEmpty(lstLimitType)) {
            unitCodeKhcn4Map.clear();
            lstLimitType.forEach(item -> unitCodeKhcn4Map.put(item.getLabelCode(), item.getLabel()));
        }
    }

    private void loadTypeUnitKhcn4Map() {
        List<CatalogDto> lstLimitType = catalogClient.filterCatalogs("KHCN", "4", "TYPE_UNIT_CODE").getData();
        if (!CollectionUtils.isEmpty(lstLimitType)) {
            typeUnitKhcn4Map.clear();
            lstLimitType.forEach(item -> typeUnitKhcn4Map.put(item.getLabelCode(), item.getLabel()));
        }
    }

    private void loadTypeCodeKhcn5Map() {
        List<CatalogDto> lstLimitType = catalogClient.filterCatalogs("KHCN", "5", "TYPE_CODE").getData();
        if (!CollectionUtils.isEmpty(lstLimitType)) {
            typeCodeKhcn5Map.clear();
            lstLimitType.forEach(item -> typeCodeKhcn5Map.put(item.getLabelCode(), item.getLabel()));
        }
    }

    private void loadSegmentCodeKhcn5Map() {
        List<CatalogDto> lstLimitType = catalogClient.filterCatalogs("KHCN", "5", "SEGMENT_CODE").getData();
        if (!CollectionUtils.isEmpty(lstLimitType)) {
            segmentCodeKhcn5Map.clear();
            lstLimitType.forEach(item -> segmentCodeKhcn5Map.put(item.getLabelCode(), item.getLabel()));
        }
    }

    private void loadTitleCodeKhcn6Map() {
        List<CatalogDto> lstLimitType = catalogClient.filterCatalogs("KHCN", "6", TITLE_CODE).getData();
        if (!CollectionUtils.isEmpty(lstLimitType)) {
            titleCodeKhcn6Map.clear();
            lstLimitType.forEach(item -> titleCodeKhcn6Map.put(item.getLabelCode(), item.getLabel()));
        }
    }

    private void loadLevelCodeKhcn6Map() {
        List<CatalogDto> lstLimitType = catalogClient.filterCatalogs("KHCN", "6", LEVEL_CODE).getData();
        if (!CollectionUtils.isEmpty(lstLimitType)) {
            levelCodeKhcn6Map.clear();
            lstLimitType.forEach(item -> levelCodeKhcn6Map.put(item.getLabelCode(), item.getLabel()));
        }
    }

    private void loadAreaKHDN() {
        List<CatalogDto> lstArea = catalogClient.filterCatalogs("KHDN", "1", "AREA_CODE").getData();
        if (!CollectionUtils.isEmpty(lstArea)) {
            areaKHDNMap.clear();
            lstArea.forEach(item -> areaKHDNMap.put(item.getLabelCode(), item.getLabel()));
        }
    }

    private void loadRegionKHDN() {
        List<CatalogDto> lstRegion = catalogClient.filterCatalogs("KHDN", "ALL", "REGION_CODE").getData();
        if (!CollectionUtils.isEmpty(lstRegion)) {
            regionKHDNMap.clear();
            lstRegion.forEach(item -> regionKHDNMap.put(item.getLabelCode(), item.getLabel()));
        }
    }

    private void loadCustSegment() {
        List<CatalogDto> lstCustSegment = catalogClient.filterCatalogs("KHDN", "2", "CUST_SEGMENT_CODE").getData();
        if (!CollectionUtils.isEmpty(lstCustSegment)) {
            custSegmentMap.clear();
            lstCustSegment.forEach(item -> custSegmentMap.put(item.getLabelCode(), item.getLabel()));
        }
    }

    private void loadRevenue() {
        List<CatalogDto> lstRevenue = catalogClient.filterCatalogs("KHDN", "3", "REVENUE_CODE").getData();
        if (!CollectionUtils.isEmpty(lstRevenue)) {
            revenueMap.clear();
            lstRevenue.forEach(item -> revenueMap.put(item.getLabelCode(), item.getLabel()));
        }
    }

    private void loadTitleKHDN() {
        List<CatalogDto> lstTitleKHDN = catalogClient.filterCatalogs("KHDN", "ALL", TITLE_CODE).getData();
        if (!CollectionUtils.isEmpty(lstTitleKHDN)) {
            titleKHDNMap.clear();
            lstTitleKHDN.forEach(item -> titleKHDNMap.put(item.getLabelCode(), item.getLabel()));
        }
    }

    private void loadJobPositionKHDN() {
        jobPositionKHDNMap.clear();
        jobPositionKHDNMap.put("CBQL", "Cán bộ quản lý");
        jobPositionKHDNMap.put("NV", "Nhân viên");
    }

    private void loadLevelKHDN() {
        List<CatalogDto> lstTitleKHDN = catalogClient.filterCatalogs("KHDN", "ALL", LEVEL_CODE).getData();
        if (!CollectionUtils.isEmpty(lstTitleKHDN)) {
            levelKHDNMap.clear();
            lstTitleKHDN.forEach(item -> levelKHDNMap.put(item.getLabelCode(), item.getLabel()));
        }
    }

    private void loadSegmentVHTD() {
        List<CatalogDto> lstSegmentVHTD = catalogClient.filterCatalogs("VHTD", "3", "GROUP_ID").getData();
        if (!CollectionUtils.isEmpty(lstSegmentVHTD)) {
            segmentVhtdMap.clear();
            lstSegmentVHTD.forEach(item -> segmentVhtdMap.put(item.getLabelCode(), item.getLabel()));
        }
    }

    private void loadTransGroupVHTD() {
        List<CatalogDto> lstTransGroupVHTD = catalogClient.filterCatalogs("VHTD", "10", "TRANS_GROUP").getData();
        if (!CollectionUtils.isEmpty(lstTransGroupVHTD)) {
            transGroupVhtdMap.clear();
            lstTransGroupVHTD.forEach(item -> transGroupVhtdMap.put(item.getLabelCode(), item.getLabel()));
        }
    }

    private void loadTransTypeVHTD() {
        List<CatalogDto> lstTransGroupVHTD = catalogClient.filterCatalogs("VHTD", "10", TRANS_TYPE).getData();
        if (!CollectionUtils.isEmpty(lstTransGroupVHTD)) {
            transTypeVhtdMap.clear();
            lstTransGroupVHTD.forEach(item -> transTypeVhtdMap.put(item.getLabelCode(), item.getLabel()));
        }
    }

    private void loadTransDtlVHTD() {
        List<CatalogDto> lstTransGroupVHTD = catalogClient.filterCatalogs("VHTD", "10", "TRANS_DETAIL").getData();
        if (!CollectionUtils.isEmpty(lstTransGroupVHTD)) {
            transDtlVhtdMap.clear();
            lstTransGroupVHTD.forEach(item -> transDtlVhtdMap.put(item.getLabelCode(), item.getLabel()));
        }
    }

    private void loadTitleVHTD() {
        List<CatalogDto> lstTitleVHTD = catalogClient.filterCatalogs("VHTD", "5", TITLE).getData();
        if (!CollectionUtils.isEmpty(lstTitleVHTD)) {
            titleVhtdMap.clear();
            lstTitleVHTD.forEach(item -> titleVhtdMap.put(item.getLabelCode(), item.getLabel()));
        }
    }

    private void loadLevelVHTD() {
        List<CatalogDto> lstLevelVHTD = catalogClient.filterCatalogs("VHTD", "5", "LVL").getData();
        if (!CollectionUtils.isEmpty(lstLevelVHTD)) {
            levelVhtdMap.clear();
            lstLevelVHTD.forEach(item -> levelVhtdMap.put(item.getLabelCode(), item.getLabel()));
        }
    }

    private void loadProductVHTD() {
        List<CatalogDto> lstProductVHTD = catalogClient.filterCatalogs("VHTD", "6", "PRODUCT").getData();
        if (!CollectionUtils.isEmpty(lstProductVHTD)) {
            productVhtdMap.clear();
            lstProductVHTD.forEach(item -> productVhtdMap.put(item.getLabelCode(), item.getLabel()));
        }
    }

    private void loadSignAddressVHTD() {
        List<CatalogDto> lstSignAddressVHTD = catalogClient.filterCatalogs("VHTD", "ALL", "PS_ADDR").getData();
        if (!CollectionUtils.isEmpty(lstSignAddressVHTD)) {
            signAddressVhtdMap.clear();
            lstSignAddressVHTD.forEach(item -> signAddressVhtdMap.put(item.getLabelCode(), item.getLabel()));
        }
    }

    private void loadSegment2VHTD() {
        List<CatalogDto> lstSegment2VHTD = catalogClient.filterCatalogs("VHTD", "3", "SEGMENT").getData();
        if (!CollectionUtils.isEmpty(lstSegment2VHTD)) {
            segment2VhtdMap.clear();
            lstSegment2VHTD.forEach(item -> segment2VhtdMap.put(item.getLabelCode(), item.getLabel()));
        }
    }

    private void loadSurveyVHTD() {
        List<CatalogDto> lstSurveyVHTD = catalogClient.filterCatalogs("VHTD", "ALL", "SURVEY").getData();
        if (!CollectionUtils.isEmpty(lstSurveyVHTD)) {
            surveyVhtdMap.clear();
            lstSurveyVHTD.forEach(item -> surveyVhtdMap.put(item.getLabelCode(), item.getLabel()));
        }
    }

    private void loadUnitVHTD() {
        List<CatalogDto> lstUnitVHTD = catalogClient.filterCatalogs("VHTD", "2", "UNIT").getData();
        if (!CollectionUtils.isEmpty(lstUnitVHTD)) {
            unitVhtdMap.clear();
            lstUnitVHTD.forEach(item -> unitVhtdMap.put(item.getLabelCode(), item.getLabel()));
        }
    }

    private void loadUnit1VHTD() {
        List<CatalogDto> lstUnit1VHTD = catalogClient.filterCatalogs("VHTD", "2", "UNIT1").getData();
        if (!CollectionUtils.isEmpty(lstUnit1VHTD)) {
            unit1VhtdMap.clear();
            lstUnit1VHTD.forEach(item -> unit1VhtdMap.put(item.getLabelCode(), item.getLabel()));
        }
    }

    private void loadUnit2VHTD() {
        List<CatalogDto> lstUnit2VHTD = catalogClient.filterCatalogs("VHTD", "2", "UNIT2").getData();
        if (!CollectionUtils.isEmpty(lstUnit2VHTD)) {
            unit2VhtdMap.clear();
            lstUnit2VHTD.forEach(item -> unit2VhtdMap.put(item.getLabelCode(), item.getLabel()));
        }
    }

    private void loadTitleMgmtVHTD() {
        List<CatalogDto> lstTitleMgmtVHTD = catalogClient.filterCatalogs("VHTD", "2", TITLE).getData();
        if (!CollectionUtils.isEmpty(lstTitleMgmtVHTD)) {
            titleMgmtVhtdMap.clear();
            lstTitleMgmtVHTD.forEach(item -> titleMgmtVhtdMap.put(item.getLabelCode(), item.getLabel()));
        }
    }

    private void loadBusinessTypeVHTD() {
        List<CatalogDto> lstBusinessTypeVHTD = catalogClient.filterCatalogs("VHTD", "3", "BUSINESS_TYPE").getData();
        if (!CollectionUtils.isEmpty(lstBusinessTypeVHTD)) {
            businessTypeVhtdMap.clear();
            lstBusinessTypeVHTD.forEach(item -> businessTypeVhtdMap.put(item.getLabelCode(), item.getLabel()));
        }
    }

    private void loadBusinessTypeSlaVHTD() {
        List<CatalogDto> lstBusinessTypeSlaVHTD = catalogClient.filterCatalogs("VHTD", "6", "BUSINESS_TYPE").getData();
        if (!CollectionUtils.isEmpty(lstBusinessTypeSlaVHTD)) {
            businessTypeSlaVhtdMap.clear();
            lstBusinessTypeSlaVHTD.forEach(item -> businessTypeSlaVhtdMap.put(item.getLabelCode(), item.getLabel()));
        }
    }

    private void loadWorkAddressVHTD() {
        List<CatalogDto> lstWorkAddressVHTD = catalogClient.filterCatalogs("VHTD", "8", "WORK_ADDR").getData();
        if (!CollectionUtils.isEmpty(lstWorkAddressVHTD)) {
            workAddressVhtdMap.clear();
            lstWorkAddressVHTD.forEach(item -> workAddressVhtdMap.put(item.getLabelCode(), item.getLabel()));
        }
    }

    private void loadLimitTypeVHTD() {
        List<CatalogDto> lstLimitTypeVHTD = catalogClient.filterCatalogs("VHTD", "7", "LIMIT_TYPE").getData();
        if (!CollectionUtils.isEmpty(lstLimitTypeVHTD)) {
            limitTypeVhtdMap.clear();
            lstLimitTypeVHTD.forEach(item -> limitTypeVhtdMap.put(item.getLabelCode(), item.getLabel()));
        }
    }

    private void loadEvaluateVHTD() {
        List<CatalogDto> lstEvaluateVHTD = catalogClient.filterCatalogs("VHTD", "ALL", "EVALUATE").getData();
        if (!CollectionUtils.isEmpty(lstEvaluateVHTD)) {
            evaluateVhtdMap.clear();
            lstEvaluateVHTD.forEach(item -> evaluateVhtdMap.put(item.getLabelCode(), item.getLabel()));
        }
    }

    private void loadTypeSutVHTD() {
        List<CatalogDto> lstTypeSutVHTD = catalogClient.filterCatalogs("VHTD", "ALL", "TYPE_SUT").getData();
        if (!CollectionUtils.isEmpty(lstTypeSutVHTD)) {
            typeSutVhtdMap.clear();
            lstTypeSutVHTD.forEach(item -> typeSutVhtdMap.put(item.getLabelCode(), item.getLabel()));
        }
    }

    private void loadTitleQsMap() {
        List<CatalogDto> lstCatalog = catalogClient.filterCatalogs("QS", "ALL", TITLE).getData();
        if (!CollectionUtils.isEmpty(lstCatalog)) {
            titleQsMap.clear();
            lstCatalog.forEach(item -> titleQsMap.put(item.getLabelCode(), item.getLabel()));
        }
    }

    private void loadLevelQsMap() {
        List<CatalogDto> lstCatalog = catalogClient.filterCatalogs("QS", "ALL", LEVEL_CODE).getData();
        if (!CollectionUtils.isEmpty(lstCatalog)) {
            levelQsMap.clear();
            lstCatalog.forEach(item -> levelQsMap.put(item.getLabelCode(), item.getLabel()));
        }
    }

    private void loadTransTypeQs1Map() {
        List<CatalogDto> lstCatalog = catalogClient.filterCatalogs("QS", "1", TRANS_TYPE).getData();
        if (!CollectionUtils.isEmpty(lstCatalog)) {
            transTypeQs1Map.clear();
            lstCatalog.forEach(item -> transTypeQs1Map.put(item.getLabelCode(), item.getLabel()));
        }
    }

    private void loadTransTypeQs2Map() {
        List<CatalogDto> lstCatalog = catalogClient.filterCatalogs("QS", "2", TRANS_TYPE).getData();
        if (!CollectionUtils.isEmpty(lstCatalog)) {
            transTypeQs2Map.clear();
            lstCatalog.forEach(item -> transTypeQs2Map.put(item.getLabelCode(), item.getLabel()));
        }
    }

    private void loadSizeQs3Map() {
        List<CatalogDto> lstCatalog = catalogClient.filterCatalogs("QS", "3", "SIZE_TYPE").getData();
        if (!CollectionUtils.isEmpty(lstCatalog)) {
            sizeQs3Map.clear();
            lstCatalog.forEach(item -> sizeQs3Map.put(item.getLabelCode(), item.getLabel()));
        }
    }

    private void loadOrgQsMap() {
        List<CatalogDto> lstCatalog = catalogClient.filterCatalogs("QS", "ALL", "ORG_TYPE").getData();
        if (!CollectionUtils.isEmpty(lstCatalog)) {
            orgQsMap.clear();
            lstCatalog.forEach(item -> orgQsMap.put(item.getLabelCode(), item.getLabel()));
        }
    }

    private void loadHasAuthQs3Map() {
        List<CatalogDto> lstCatalog = catalogClient.filterCatalogs("QS", "3", "HAS_AUTH").getData();
        if (!CollectionUtils.isEmpty(lstCatalog)) {
            hasAuthQs3Map.clear();
            lstCatalog.forEach(item -> hasAuthQs3Map.put(item.getLabelCode(), item.getLabel()));
        }
    }

    private void loadManageScaleQs3Map() {
        List<CatalogDto> lstCatalog = catalogClient.filterCatalogs("QS", "3", "MANAGE_SCALE").getData();
        if (!CollectionUtils.isEmpty(lstCatalog)) {
            manageScaleQs3Map.clear();
            lstCatalog.forEach(item -> manageScaleQs3Map.put(item.getLabelCode(), item.getLabel()));
        }
    }

    private void loadAssetScaleQs4Map() {
        List<CatalogDto> lstCatalog = catalogClient.filterCatalogs("QS", "ALL", "ASSET_SCALE").getData();
        if (!CollectionUtils.isEmpty(lstCatalog)) {
            assetScaleQs4Map.clear();
            lstCatalog.forEach(item -> assetScaleQs4Map.put(item.getLabelCode(), item.getLabel()));
        }
    }

    private void loadTypeQs7Map() {
        List<CatalogDto> lstCatalog = catalogClient.filterCatalogs("QS", "7", "TYPE").getData();
        if (!CollectionUtils.isEmpty(lstCatalog)) {
            typeQs7Map.clear();
            lstCatalog.forEach(item -> typeQs7Map.put(item.getLabelCode(), item.getLabel()));
        }
    }

    private void loadStdTypeQsMap() {
        List<CatalogDto> lstCatalog = catalogClient.filterCatalogs("QS", "ALL", "STD_TYPE").getData();
        if (!CollectionUtils.isEmpty(lstCatalog)) {
            stdTypeQsMap.clear();
            lstCatalog.forEach(item -> stdTypeQsMap.put(item.getLabelCode(), item.getLabel()));
        }
    }

    private void loadUnitQs10Map() {
        List<CatalogDto> lstCatalog = catalogClient.filterCatalogs("QS", "10", "ADD_CODE").getData();
        if (!CollectionUtils.isEmpty(lstCatalog)) {
            unitQs10Map.clear();
            lstCatalog.forEach(item -> unitQs10Map.put(item.getLabelCode(), item.getLabel()));
        }
    }

    private void loadProdQs10Map() {
        List<CatalogDto> lstCatalog = catalogClient.filterCatalogs("QS", "10", PROD_CODE).getData();
        if (!CollectionUtils.isEmpty(lstCatalog)) {
            prodQs10Map.clear();
            lstCatalog.forEach(item -> prodQs10Map.put(item.getLabelCode(), item.getLabel()));
        }
    }

    private void loadRegionQsMap() {
        List<CatalogDto> lstCatalog = catalogClient.filterCatalogs("QS", "ALL", "REGION_CODE").getData();
        if (!CollectionUtils.isEmpty(lstCatalog)) {
            regionQsMap.clear();
            lstCatalog.forEach(item -> regionQsMap.put(item.getLabelCode(), item.getLabel()));
        }
    }

    @Scheduled(fixedRateString = "${cache.expireInSeconds:86400000}")
    private void autoLoad() {
        loadBranches();
        loadStreams();
        loadProducts();
        loadSubProducts();
        loadPlanGroup();
        loadPlanType();
        loadApproveLevel();
        loadAreaCode();
        loadApprovalResult();
        loadApprovalForm();
        loadGroup();
        loadCustomerVip();
        loadPlanStatus();
        loadSubUnit2();
        loadTitle();
        loadLevel();
        loadLineNumMap();
        loadDefaultWeightTypeTDPD5Map();
        loadCustTypeTDPD();
        loadStreamTDPD3();
        loadRoleTDPD();
        loadRoleTDPD10();
        loadExtraApprCoeffType();
        loadMngUnitTDPD6();
        loadTitleTDPD6Map();
        loadTitleTDPD7Map();
        loadTitleTDPD9Map();
        loadPlanGroupTDPDMap();
        loadPlanTypeTDPD1Map();
        loadWeightTypeTDPD4Map();
        loadWeightTypeTDPD5Map();
        loadLimitTypeTDPD7Map();
        loadGrProdCodeKhcn1Map();
        loadProdCodeKhcn1Map();
        loadProdCodeKhcn2Map();
        loadAddKhcn2Map();
        loadTitleKhcn2Map();
        loadGoalCodeKhcn3Map();
        loadGrProdCodeKhcn4Map();
        loadProdCodeKhcn4Map();
        loadUnitCodeKhcn4Map();
        loadTypeUnitKhcn4Map();
        loadTypeCodeKhcn5Map();
        loadSegmentCodeKhcn5Map();
        loadTitleCodeKhcn6Map();
        loadLevelCodeKhcn6Map();
        loadAreaKHDN();
        loadRegionKHDN();
        loadCustSegment();
        loadRevenue();
        loadTitleKHDN();
        loadJobPositionKHDN();
        loadLevelKHDN();
        loadTransGroupVHTD();
        loadTransTypeVHTD();
        loadTransDtlVHTD();
        loadTitleVHTD();
        loadLevelVHTD();
        loadProductVHTD();
        loadSignAddressVHTD();
        loadSegment2VHTD();
        loadSurveyVHTD();
        loadUnitVHTD();
        loadUnit1VHTD();
        loadUnit2VHTD();
        loadTitleMgmtVHTD();
        loadBusinessTypeVHTD();
        loadBusinessTypeSlaVHTD();
        loadWorkAddressVHTD();
        loadLimitTypeVHTD();
        loadEvaluateVHTD();
        loadTypeSutVHTD();
        loadTitleQsMap();
        loadLevelQsMap();
        loadTransTypeQs1Map();
        loadTransTypeQs2Map();
        loadSizeQs3Map();
        loadOrgQsMap();
        loadHasAuthQs3Map();
        loadManageScaleQs3Map();
        loadAssetScaleQs4Map();
        loadTypeQs7Map();
        loadStdTypeQsMap();
        loadUnitQs10Map();
        loadProdQs10Map();
        loadRegionQsMap();
    }

    private Map loadRunnableByName(Map<?, ?> map, Runnable function) {
        //Lấy tên method getter trước hàm loadRunnableByName này
        String currentMethod = Thread.currentThread().getStackTrace()[2].getMethodName();

        //Nếu map cache không có dữ liệu và method trước đó vẫn còn trong lst thì run function
        //remove method khỏi lst nếu map có dữ liệu
        try {
            if (!CollectionUtils.isEmpty(map)) {
                lstMethod.remove(currentMethod);
            }
            if (CollectionUtils.isEmpty(map) && lstMethod.contains(currentMethod)) {
                function.run();
            }
            return map;
        } catch (Exception e) {
            log.info(CommonCacheService.class.getName() + ". Method " + currentMethod + " fail with exception: " + e.getMessage());
            return map;
        }
    }
}
