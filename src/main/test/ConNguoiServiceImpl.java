package vn.gtel.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.gtel.common.data.service.BaseJdbcServiceImpl;
import vn.gtel.common.exception.BaseException;
import vn.gtel.common.util.CommonUtil;
import vn.gtel.common.util.SearchUtil;
import vn.gtel.model.constants.ErrorCode;
import vn.gtel.model.dto.ConNguoiDTO;
import vn.gtel.model.entity.ConNguoi;
import vn.gtel.model.enumeration.ErrorEnum;
import vn.gtel.repo.ConNguoiRepository;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class ConNguoiServiceImpl extends BaseJdbcServiceImpl<ConNguoi, String> implements ConNguoiService {

    private final Logger log = LoggerFactory.getLogger(ConNguoiServiceImpl.class);

    @Autowired
    private ConNguoiRepository conNguoiRepository;

    public ConNguoiServiceImpl(DataSource dataSource, EntityManager entityManager, ConNguoiRepository conNguoiRepository) {
        super(dataSource, entityManager, ConNguoi.class);
        this.conNguoiRepository = conNguoiRepository;
    }

    @Override
    public String create(ConNguoiDTO request) {
        log.debug("Request to save ConNguoi : {}", request);
        ConNguoi ett = new ConNguoi();
        BeanUtils.copyProperties(request, ett);
        ett.setId(UUID.randomUUID().toString());
        conNguoiRepository.save(ett);
        return ett.getId();
    }

    @Override
    public ConNguoiDTO update(ConNguoiDTO request) {
        log.debug("Request to update ConNguoi : {}", request);
        ConNguoi ett = new ConNguoi();
        BeanUtils.copyProperties(request, ett);
        conNguoiRepository.save(ett);
        return request;
    }

    @Override
    public List<ConNguoiDTO> advancedSearch(ConNguoiDTO request, Pageable pageable) {
        log.debug("Request to search ConNguoi : {}", request);
        List<Specification<ConNguoi>> list = getAdvanceSearchSpecList(request);
        Page<ConNguoi> page;
        if (!CommonUtil.listIsEmptyOrNull(list)) {
            Specification<ConNguoi> spec = list.get(0);
            for (int i = 1; i < list.size(); i++) {
                spec = spec.and(list.get(i));
            }
            page = conNguoiRepository.findAll(spec, pageable);
        } else {
            page = conNguoiRepository.findAll(pageable);
        }

        if (page.getTotalElements() > 0) {
            return page.map(existing -> {
                ConNguoiDTO dto = new ConNguoiDTO();
                try {
                    BeanUtils.copyProperties(existing, dto);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return dto;
            }).stream().collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    private List<Specification<ConNguoi>> getAdvanceSearchSpecList(
            ConNguoiDTO request) {
        List<Specification<ConNguoi>> spec = new ArrayList<>();
        if (!CommonUtil.isNullOrEmpty(request.getName())) {
            spec.add(SearchUtil.eq("name", request.getName()));
        }
        if (!CommonUtil.isNullOrEmpty(request.getIdNo())) {
            spec.add(SearchUtil.eq("idNo", request.getIdNo()));
        }
        if (!CommonUtil.isNullOrEmpty(request.getStatus())) {
            spec.add(SearchUtil.eq("status", request.getStatus()));
        }
        if (request.getBirthDay() != null) {
            spec.add(SearchUtil.eq("birthDay", request.getBirthDay()));
        }
        return spec;
    }

    @Override
    public void delete(String id) throws BaseException {
        log.debug("Request to delete ConNguoi : {}", id);
        if (CommonUtil.isNullOrEmpty(id)) {
            throw new BaseException(ErrorEnum.APP_VALID_ERROR_IS_NOT_NULL, id, null);
        } else {
            conNguoiRepository.deleteById(id);
        }
    }

    @Override
    public void approve(String id) throws BaseException{
        log.debug("Request to approve ConNguoi : {}", id);
        if (CommonUtil.isNullOrEmpty(id)) {
            throw new BaseException(ErrorEnum.APP_VALID_ERROR_IS_NOT_NULL, id, null);
        } else {
            conNguoiRepository.approve("A", id);
        }
    }
}
