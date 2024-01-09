package com.fw.channel.security.repo.impl;

import com.fw.channel.security.repo.SUserRepository;
import com.fw.model.entity.sys.SSite;
import com.fw.model.entity.sys.SUser;
import com.fw.model.entity.sys.SUserSite;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collections;
import java.util.List;

/**
 * @author thangnq.os
 * @version 1.0
 */
@Repository
public class SUserRepositoryImpl implements SUserRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public SUser getUserByUserName(String userName) {
        String sql = "SELECT u FROM SUser u WHERE u.username = :userName";
        List<SUser> list = entityManager.createQuery(sql, SUser.class).setParameter("userName", userName)
                .getResultList();
        entityManager.clear();
        if (list != null && !list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public SUser getUserById(long userId) {
        return entityManager.find(SUser.class, userId);
    }

    @Override
    public List<SUserSite> getListUserSiteByUserId(long userId) {
        String sql = "SELECT u FROM SUserSite u WHERE u.userId = :userId";
        List<SUserSite> listBranch = entityManager.createQuery(sql, SUserSite.class)
                .setParameter("userId", userId).getResultList();
        if (listBranch != null && !listBranch.isEmpty()) {
            return listBranch;
        }
        return Collections.emptyList();
    }

    @Override
    public SUserSite getUserSiteByUserId(long userId) {
        String sql = "SELECT u FROM SUserSite u WHERE u.userId = :userId";
        List<SUserSite> list = entityManager.createQuery(sql, SUserSite.class)
                .setParameter("userId", userId).getResultList();
        if (list != null && !list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public SSite getSiteById(long siteId) {
        return entityManager.find(SSite.class, siteId);
    }
}
