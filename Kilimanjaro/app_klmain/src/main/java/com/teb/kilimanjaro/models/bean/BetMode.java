package com.teb.kilimanjaro.models.bean;

/**
 * Created by yangbin on 16/7/20.
 * 解析assets文件夹里面的文件对象
 */
public class BetMode {
    private String modeName;// 名称
    private int status;// 是否显示
    private int modeCode;// code
    private String modeNums;// 对应的的号码

    public void setModeNums(String modeNums) {
        this.modeNums = modeNums;
    }

    public String[] getModeNums() {
        return modeNums.split(",");
    }

    public String getModeName() {
        return modeName;
    }

    public void setModeName(String modeName) {
        this.modeName = modeName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getModeCode() {
        return modeCode;
    }

    public void setModeCode(int modeCode) {
        this.modeCode = modeCode;
    }

    @Override
    public String toString() {
        return "BetMode{" +
                "modeName='" + modeName + '\'' +
                ", status=" + status +
                ", modeCode=" + modeCode +
                '}';
    }
}
