package com.three.transfer.enums;

public enum TFileLinkStateEnum {
    SUCCESS(0, "添加成功"), INNER_ERROR(-1001, "操作失败");


    private int state;
    private String stateInfo;

    public int getState() {
        return state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    private TFileLinkStateEnum(int state, String stateInfo) {
        this.state = state;
        this.stateInfo = stateInfo;
    }

    public static TFileLinkStateEnum stateOf(int index) {
        for (TFileLinkStateEnum state : values()){
            if (state.getState() == index) {
                return state;
            }
        }
        return null;
    }






}
