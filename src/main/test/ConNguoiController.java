package vn.gtel.api.ConNguoi;

import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import vn.gtel.common.exception.BaseException;
import vn.gtel.common.util.RequestUtil;
import vn.gtel.common.util.SearchUtil;
import vn.gtel.model.dto.ConNguoiDTO;
import vn.gtel.model.common.SuccessResponse;
import vn.gtel.model.enumeration.SortOrderEnum;
import vn.gtel.service.ConNguoiService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/con-nguoi")
@Tag(name = "ConNguoi Controller")
public class ConNguoiController {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ConNguoiService conNguoiService;

    @PostMapping("/create")
    @ApiOperation("Create con nguoi")
    public SuccessResponse<String> create(@RequestBody ConNguoiDTO request) {
        String id = conNguoiService.create(request);
        return RequestUtil.ok(id);
    }

    @PutMapping("/update")
    @ApiOperation("Update con nguoi")
    public SuccessResponse update(@RequestBody ConNguoiDTO request) throws BaseException{
        conNguoiService.update(request)
        return new SuccessResponse();
    }

    @PostMapping("/search")
    @ApiOperation("Search con nguoi")
    public SuccessResponse<List<ConNguoiDTO>> advancedSearch(
            @RequestBody ConNguoiDTO request, @PositiveOrZero @RequestParam(required = false, defaultValue = "0") Integer page,
            @Positive @RequestParam(required = false) Integer size, @RequestParam(required = false) String sort,
            @RequestParam(required = false) SortOrderEnum order
    ) {
        Pageable pageable = SearchUtil.getPageableFromParam(page, size, sort, order);
        return RequestUtil.ok(conNguoiService.advancedSearch(request, pageable));
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation("Delete con nguoi")
    public SuccessResponse delete(@PathVariable String id) throws BaseException {
        conNguoiService.delete(id);
        return new SuccessResponse();
    }

    @PutMapping("/approve/{id}")
    @ApiOperation("Duyet con nguoi")
    public SuccessResponse approve(@PathVariable String id) throws BaseException {
        conNguoiService.approve(id);
        return new SuccessResponse();
    }

    @GetMapping("/nghe-nghiep/{id}")
    @ApiOperation("Lay thong tin nghe nghiep cua con nguoi")
    public SuccessResponse getInfo(@PathVariable String id) throws BaseException {
        conNguoiService.approve(id);
        return new SuccessResponse();
    }
}
