package com.three.transfer.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class TFileLink {
    private int fileLinkId;
    private String fileLinkAddr;
    private String fileLinkPassword;
    private int fileLinkAccessTimes;
    private Long fileLinkValidTime;
    private Date createTime;
    private Date lastEditTime;
    private TFile file;

}
