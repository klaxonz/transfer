package com.three.transfer.dto;

import com.three.transfer.entity.TFile;
import com.three.transfer.enums.TFileStateEnum;
import java.util.List;

public class TFileExecution {

    private int state;
    private String stateInfo;

    private TFile file;
    private List<TFile> fileList;

    public TFileExecution() {

    }

    //操作失败时使用的构造器
    public TFileExecution(TFileStateEnum stateEnum) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
    }

    //操作成功时使用的构造器
    public TFileExecution(TFileStateEnum stateEnum, TFile file) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
        this.file = file;
    }

    //操作成功时使用的构造器
    public TFileExecution(TFileStateEnum stateEnum, List<TFile> fileList) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
        this.fileList = fileList;
    }


    public int getState() {
        return state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public TFile getFile() {
        return file;
    }

    public List<TFile> getFileList() {
        return fileList;
    }
}
