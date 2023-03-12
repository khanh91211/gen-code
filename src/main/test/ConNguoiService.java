package vn.gtel.service;


import org.springframework.data.domain.Pageable;
import vn.gtel.common.exception.BaseException;
import vn.gtel.model.dto.ConNguoiDTO;

import java.util.List;

public interface ConNguoiService { //extends BaseJdbcService<User, String>
    String create(ConNguoiDTO request);

    ConNguoiDTO update(ConNguoiDTO request);

    List<ConNguoiDTO> advancedSearch(ConNguoiDTO request, Pageable pageable);

    void delete(String id) throws BaseException;

    void approve(String id) throws BaseException;
}
