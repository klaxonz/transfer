package com.three.transfer.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;


@Setter
@Getter
public class TFile {
    private int fileId;
    private String fileName;
    private String filePath;
    private Long fileSize;
    private Date createTime;
    private Date lastEditTime;
    private long fileValidTimeMills;
    private int fileDownloadTime;
    private TFileCategory fileCategory;
    private User user;
}
