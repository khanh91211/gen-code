package vn.gtel.model.entity;

import lombok.Getter;
import lombok.Setter;
import vn.gtel.model.common.EntityBase;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@Entity(name = "ConNguoi")
@Table(name = "DB_CONNGUOI")
public class ConNguoi extends EntityBase implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column(name = "NAME")
    private String name;

    @Temporal(TemporalType.DATE)
    @Column(name = "BIRTH_DAY")
    private Date birthDay;

    @Column(name = "ID_NO")
    private String idNo;

    @Column(name = "STATUS")
    private String status;
}