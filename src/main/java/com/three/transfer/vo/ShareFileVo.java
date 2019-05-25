package com.three.transfer.vo;

public class ShareFileVo {

    private Integer fileId;
    private String fileName;
    private Long fileSize;
    private boolean hasPassword;
    private String fileValidTime;

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
        this.fileName = fileName;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public boolean isHasPassword() {
        return hasPassword;
    }

    public void setHasPassword(boolean hasPassword) {
        this.hasPassword = hasPassword;
    }

    public String getFileValidTime() {
        return fileValidTime;
    }

    public void setFileValidTime(String fileValidTime) {
        this.fileValidTime = fileValidTime;
    }
}
