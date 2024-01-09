package com.fw.channel.security.dto;

import java.util.ArrayList;
import java.util.List;

public class GetUserInfoRepsonse {

    private List<MRightResponseDto> rights = new ArrayList<>();
    private List<MMenuResponseDto> menus = new ArrayList<>();
    private List<MMenuOrsResponseDto> menusOrs = new ArrayList<>();

    private List<String> scopeIds = new ArrayList<>();

    public List<MRightResponseDto> getRights() {
        return rights;
    }

    public void setRights(List<MRightResponseDto> rights) {
        this.rights = rights;
    }

    public List<MMenuResponseDto> getMenus() {
        return menus;
    }

    public void setMenus(List<MMenuResponseDto> menus) {
        this.menus = menus;
    }

    public List<String> getScopeIds() {
        return scopeIds;
    }

    public void setScopeIds(List<String> scopeIds) {
        this.scopeIds = scopeIds;
    }

    public List<MMenuOrsResponseDto> getMenusOrs() {
        return menusOrs;
    }

    public void setMenusOrs(List<MMenuOrsResponseDto> menusOrs) {
        this.menusOrs = menusOrs;
    }
}
