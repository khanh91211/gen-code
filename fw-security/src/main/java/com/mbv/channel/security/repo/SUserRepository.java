package com.fw.channel.security.repo;

import com.fw.model.entity.sys.SSite;
import com.fw.model.entity.sys.SUser;
import com.fw.model.entity.sys.SUserSite;

import java.util.List;

public interface SUserRepository {
    SUser getUserByUserName(String userName);
    SUser getUserById(long userId);
    List<SUserSite> getListUserSiteByUserId(long userId);
    SUserSite getUserSiteByUserId(long userId);
    SSite getSiteById(long siteId);
}
