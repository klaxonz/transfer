package com.three.transfer.entity;

import java.util.Date;

public class TFileCategory {
    private Integer fileCategoryId;

    private String fileCategoryName;

    private Integer fileCategoryFormat;

    private Date createTime;

    public TFileCategory(Integer fileCategoryId, String fileCategoryName, Integer fileCategoryFormat, Date createTime) {
        this.fileCategoryId = fileCategoryId;
        this.fileCategoryName = fileCategoryName;
        this.fileCategoryFormat = fileCategoryFormat;
        this.createTime = createTime;
    }

    public TFileCategory() {
        super();
    }

    public Integer getFileCategoryId() {
        return fileCategoryId;
    }

    public void setFileCategoryId(Integer fileCategoryId) {
        this.fileCategoryId = fileCategoryId;
    }

    public String getFileCategoryName() {
        return fileCategoryName;
    }

    public void setFileCategoryName(String fileCategoryName) {
        this.fileCategoryName = fileCategoryName == null ? null : fileCategoryName.trim();
    }

    public Integer getFileCategoryFormat() {
        return fileCategoryFormat;
    }

    public void setFileCategoryFormat(Integer fileCategoryFormat) {
        this.fileCategoryFormat = fileCategoryFormat;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}