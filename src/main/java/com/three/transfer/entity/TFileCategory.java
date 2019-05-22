package com.three.transfer.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class TFileCategory {
    private int fileCategoryId;
    private String fileCategoryName;
    private int fileCategoryFormat;
    private Date createTime;
}
