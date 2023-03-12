package vn.gtel.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class ConNguoiDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private Date birthDay;
    private String idNo;
    private String status;
    private String createdBy;
    private Date createdDate;
    private String modifiedBy;
    private Date modifiedDate;
}