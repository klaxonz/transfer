package com.three.transfer.entity;

import java.util.Date;

public class TFile {
    private Integer fileId;

    private String fileName;

    private Integer fileCategoryId;

    private Integer userId;

    private String filePath;

    private Date createTime;

    private Date lastEditTime;

    private Integer fileDownloadTime;

    private Long fileSize;

    private Date fileValidTime;

    public TFile(Integer fileId, String fileName, Integer fileCategoryId, Integer userId, String filePath, Date createTime, Date lastEditTime, Integer fileDownloadTime, Long fileSize, Date fileValidTime) {
        this.fileId = fileId;
        this.fileName = fileName;
        this.fileCategoryId = fileCategoryId;
        this.userId = userId;
        this.filePath = filePath;
        this.createTime = createTime;
        this.lastEditTime = lastEditTime;
        this.fileDownloadTime = fileDownloadTime;
        this.fileSize = fileSize;
        this.fileValidTime = fileValidTime;
    }

    public TFile() {
        super();
    }

    public Integer getFileId() {
        return fileId;
    }

    public void setFileId(Integer fileId) {
        this.fileId = fileId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName == null ? null : fileName.trim();
    }

    public Integer getFileCategoryId() {
        return fileCategoryId;
    }

    public void setFileCategoryId(Integer fileCategoryId) {
        this.fileCategoryId = fileCategoryId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath == null ? null : filePath.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastEditTime() {
        return lastEditTime;
    }

    public void setLastEditTime(Date lastEditTime) {
        this.lastEditTime = lastEditTime;
    }

    public Integer getFileDownloadTime() {
        return fileDownloadTime;
    }

    public void setFileDownloadTime(Integer fileDownloadTime) {
        this.fileDownloadTime = fileDownloadTime;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public Date getFileValidTime() {
        return fileValidTime;
    }

    public void setFileValidTime(Date fileValidTime) {
        this.fileValidTime = fileValidTime;
    }
}