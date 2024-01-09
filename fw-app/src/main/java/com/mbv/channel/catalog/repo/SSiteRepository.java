package com.fw.channel.catalog.repo;

import com.fw.model.entity.sys.SSite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SSiteRepository extends  JpaRepository<SSite, Long>{

    @Query("Select distinct s from SSite s join SUserSite us ON s.id=us.siteId Where us.userId= :userId")
    List<SSite> findByUserId(@Param("userId") Long userId);


    @Query("Select s from SSite s")
    List<SSite> getSiteList();


    SSite getById( Long id);


    @Query("Select s from SSite s Where s.code = :code and s.isLastest = false")
    List<SSite> getByCode(@Param("code") String code);

}
