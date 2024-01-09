package com.fw.channel.security.dto;

public class MMenuOrsResponseDto {
    private String menuId;
    private String menuName;
    private String menuNameEng;
    private String parentId;
    private String visible;
    private String menuType;
    private String menuLevel;
    private String contentUrl;
    private String tbsvUsername;

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getMenuType() {
        return menuType;
    }

    public void setMenuType(String menuType) {
        this.menuType = menuType;
    }

    public String getMenuLevel() {
        return menuLevel;
    }

    public void setMenuLevel(String menuLevel) {
        this.menuLevel = menuLevel;
    }

    public String getContentUrl() {
        return contentUrl;
    }
    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }
    public String getVisible() {
        return visible;
    }

    public void setVisible(String visible) {
        this.visible = visible;
    }

    public String getTbsvUsername() {
        return tbsvUsername;
    }

    public void setTbsvUsername(String tbsvUsername) {
        this.tbsvUsername = tbsvUsername;
    }

    public String getMenuNameEng() {
        return menuNameEng;
    }

    public void setMenuNameEng(String menuNameEng) {
        this.menuNameEng = menuNameEng;
    }
}
