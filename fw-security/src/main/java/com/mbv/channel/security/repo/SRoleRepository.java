package com.fw.channel.security.repo;

import com.fw.model.entity.sys.SRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SRoleRepository extends JpaRepository<SRole, Long> {
    boolean existsById(Long id);
    SRole getById(Long roleId);

    @Query("select distinct ro from SRole ro\n" +
            "join SGroupRole gr on ro.id=gr.roleId\n" +
            "join SGroup g on gr.groupId=g.id\n" +
            "join SUserGroup ug on g.id=ug.groupId\n" +
            "join SUser u on ug.userId=u.id\n" +
            "where u.id=:userId")
    List<SRole> getRoleByUserId(@Param("userId") Long userId);
}
