package com.three.transfer.dto;

import com.three.transfer.entity.TFileLink;
import com.three.transfer.enums.TFileLinkStateEnum;

import java.util.List;

public class TFileLinkExecution {

    private int state;
    private String stateInfo;

    private TFileLink fileLink;
    private List<TFileLink> fileLinkList;

    public TFileLinkExecution() {

    }

    //操作失败时使用的构造器
    public TFileLinkExecution(TFileLinkStateEnum stateEnum) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
    }

    //操作成功时使用的构造器
    public TFileLinkExecution(TFileLinkStateEnum stateEnum, TFileLink fileLink) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
        this.fileLink = fileLink;
    }

    //操作成功时使用的构造器
    public TFileLinkExecution(TFileLinkStateEnum stateEnum, List<TFileLink> fileLinkList) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
        this.fileLinkList = fileLinkList;
    }


    public int getState() {
        return state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public TFileLink getFileLink() {
        return fileLink;
    }

    public List<TFileLink> getFileLinkList() {
        return fileLinkList;
    }
}
