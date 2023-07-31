package com.genCode.dto;

public class EntitiesDto {
    private String isActive;
    private String entityKey;
    private String packageKey;
    private String typeKey;
    private String lineKey;
    private String nameDisplay;
    private String genCtrl;
    private String genEntity;
    private String genService;
    private String genRepo;
    private String excelName;

    //get set

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getPackageKey() {
        return packageKey;
    }

    public void setPackageKey(String packageKey) {
        this.packageKey = packageKey;
    }

    public String getEntityKey() {
        return entityKey;
    }

    public void setEntityKey(String entityKey) {
        this.entityKey = entityKey;
    }

    public String getTypeKey() {
        return typeKey;
    }

    public void setTypeKey(String typeKey) {
        this.typeKey = typeKey;
    }

    public String getLineKey() {
        return lineKey;
    }

    public void setLineKey(String lineKey) {
        this.lineKey = lineKey;
    }

    public String getNameDisplay() {
        return nameDisplay;
    }

    public void setNameDisplay(String nameDisplay) {
        this.nameDisplay = nameDisplay;
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

    public String getExcelName() {
        return excelName;
    }

    public void setExcelName(String excelName) {
        this.excelName = excelName;
    }
}
