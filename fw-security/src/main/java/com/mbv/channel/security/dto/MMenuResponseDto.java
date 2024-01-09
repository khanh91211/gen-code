package com.fw.channel.security.dto;

public class MMenuResponseDto {
    private Long id;
    private Long parentId;
    private String name;
    private String url;
    private Long position;
    private String type;
    private String icon;
    private Boolean active;
    private Boolean activeService;
    private String typeLink;
    private Long menuMicroId;
    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getPosition() {
        return position;
    }

    public void setPosition(Long position) {
        this.position = position;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean getActiveService() {
        return activeService;
    }

    public void setActiveService(Boolean activeService) {
        this.activeService = activeService;
    }

    public String getTypeLink() {
        return typeLink;
    }

    public void setTypeLink(String typeLink) {
        this.typeLink = typeLink;
    }

    public Long getMenuMicroId() {
        return menuMicroId;
    }

    public void setMenuMicroId(Long menuMicroId) {
        this.menuMicroId = menuMicroId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
