package com.three.transfer.entity;

import java.util.Date;

public class TFileLink {
    private Integer fileLinkId;

    private String fileLinkAddr;

    private Integer fileId;

    private Integer fileLinkAccessTimes;

    private Date createTime;

    private Date lastEditTime;

    private String fileLinkPassword;

    private Long fileLinkValidTime;

    public TFileLink(Integer fileLinkId, String fileLinkAddr, Integer fileId, Integer fileLinkAccessTimes, Date createTime, Date lastEditTime, String fileLinkPassword, Long fileLinkValidTime) {
        this.fileLinkId = fileLinkId;
        this.fileLinkAddr = fileLinkAddr;
        this.fileId = fileId;
        this.fileLinkAccessTimes = fileLinkAccessTimes;
        this.createTime = createTime;
        this.lastEditTime = lastEditTime;
        this.fileLinkPassword = fileLinkPassword;
        this.fileLinkValidTime = fileLinkValidTime;
    }

    public TFileLink() {
        super();
    }

    public Integer getFileLinkId() {
        return fileLinkId;
    }

    public void setFileLinkId(Integer fileLinkId) {
        this.fileLinkId = fileLinkId;
    }

    public String getFileLinkAddr() {
        return fileLinkAddr;
    }

    public void setFileLinkAddr(String fileLinkAddr) {
        this.fileLinkAddr = fileLinkAddr == null ? null : fileLinkAddr.trim();
    }

    public Integer getFileId() {
        return fileId;
    }

    public void setFileId(Integer fileId) {
        this.fileId = fileId;
    }

    public Integer getFileLinkAccessTimes() {
        return fileLinkAccessTimes;
    }

    public void setFileLinkAccessTimes(Integer fileLinkAccessTimes) {
        this.fileLinkAccessTimes = fileLinkAccessTimes;
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

    public String getFileLinkPassword() {
        return fileLinkPassword;
    }

    public void setFileLinkPassword(String fileLinkPassword) {
        this.fileLinkPassword = fileLinkPassword == null ? null : fileLinkPassword.trim();
    }

    public Long getFileLinkValidTime() {
        return fileLinkValidTime;
    }

    public void setFileLinkValidTime(Long fileLinkValidTime) {
        this.fileLinkValidTime = fileLinkValidTime;
    }
}