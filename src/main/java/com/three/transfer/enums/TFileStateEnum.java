package com.three.transfer.enums;

public enum TFileStateEnum {
    SUCCESS(0, "添加成功"), INNER_ERROR(-1001, "操作失败");


    private int state;
    private String stateInfo;

    public int getState() {
        return state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    private TFileStateEnum(int state, String stateInfo) {
        this.state = state;
        this.stateInfo = stateInfo;
    }

    public static TFileStateEnum stateOf(int index) {
        for (TFileStateEnum state : values()){
            if (state.getState() == index) {
                return state;
            }
        }
        return null;
    }






}
