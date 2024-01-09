package com.fw.channel.security.repo;

import java.util.List;

import com.fw.model.entity.sys.SRight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SRightRepository extends JpaRepository<SRight, Long> {
    SRight getById(Long id);

    @Query("select distinct ri from SRight ri\n" +
            "join SRoleRight rr on ri.id=rr.rightId\n" +
            "join  SRole ro on rr.roleId=ro.id\n" +
            "join SGroupRole gr on ro.id=gr.roleId\n" +
            "join SGroup g on gr.groupId=g.id\n" +
            "join SUserGroup ug on g.id=ug.groupId\n" +
            "join SUser u on ug.userId=u.id\n" +
            "where u.id=:userId")
    List<SRight> getRightByUserId(@Param("userId") Long userId);
}
