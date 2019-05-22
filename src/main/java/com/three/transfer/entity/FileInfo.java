package com.three.transfer.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;


@Getter
@Setter
public class FileInfo {
    private int fileId;
    private String fileName;
    private long fileSize;
    private Date uploadTime;
    private Date validTime;
    private int fileDownloadTime;
    private int userId;
    private int fileCategoryId;
}
