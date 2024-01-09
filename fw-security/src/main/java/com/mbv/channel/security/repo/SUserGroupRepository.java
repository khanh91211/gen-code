package com.fw.channel.security.repo;

import java.util.List;

import com.fw.model.entity.sys.SGroup;
import com.fw.model.entity.sys.SUserGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface SUserGroupRepository extends JpaRepository<SUserGroup, Long> {
    void deleteAllByUserId(Long userId);

    void deleteAllByGroupId(Long groupId);

    @Query("SELECT g FROM SGroup g join SUserGroup ug ON g.id=ug.groupId WHERE ug.userId = :userId")
    List<SGroup> getGroupsByUserId(@Param("userId") Long userId);

    @Query("SELECT g.superUser FROM SGroup g join SUserGroup ug ON g.id=ug.groupId WHERE ug.userId = :userId AND g.superUser = true")
    Boolean isSuperUser(@Param("userId") Long userId);

    @Query("SELECT g FROM SGroup g WHERE g.superUser = true")
    SGroup getSuperUserGroup();
}
