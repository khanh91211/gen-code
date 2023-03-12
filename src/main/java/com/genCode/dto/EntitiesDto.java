package com.genCode.dto;

public class EntitiesDto {
    private String isActive;
    private String entityKey;
    private String module;
    private String nameProperty;
    private String nameColumn;
    private String nameDisplay;
    private String tableName;
    private String genCtrl;
    private String genEntity;
    private String genService;
    private String genRepo;

    //get set

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getEntityKey() {
        return entityKey;
    }

    public void setEntityKey(String entityKey) {
        this.entityKey = entityKey;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getNameProperty() {
        return nameProperty;
    }

    public void setNameProperty(String nameProperty) {
        this.nameProperty = nameProperty;
    }

    public String getNameColumn() {
        return nameColumn;
    }

    public void setNameColumn(String nameColumn) {
        this.nameColumn = nameColumn;
    }

    public String getNameDisplay() {
        return nameDisplay;
    }

    public void setNameDisplay(String nameDisplay) {
        this.nameDisplay = nameDisplay;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getGenCtrl() {
        return genCtrl;
    }

    public void setGenCtrl(String genCtrl) {
        this.genCtrl = genCtrl;
    }

    public String getGenEntity() {
        return genEntity;
    }

    public void setGenEntity(String genEntity) {
        this.genEntity = genEntity;
    }

    public String getGenService() {
        return genService;
    }

    public void setGenService(String genService) {
        this.genService = genService;
    }

    public String getGenRepo() {
        return genRepo;
    }

    public void setGenRepo(String genRepo) {
        this.genRepo = genRepo;
    }
}
