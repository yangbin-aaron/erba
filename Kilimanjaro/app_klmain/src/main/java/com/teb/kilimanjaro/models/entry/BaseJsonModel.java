package com.teb.kilimanjaro.models.entry;

/**
 * Created by yangbin on 16/7/6.
 * json返回数据公告类
 */
public class BaseJsonModel {
    private int status;
    private String message;

    /**
     * 返回数据是否成功
     *
     * @return
     */
    public boolean isSuccess() {
        return status == 200;
    }

    /**
     * Token是否被修改（被强制推下线，1小时未操作APP）
     * @return
     */
    public boolean isUpdateToken() {
        return status == 403;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String toMyString() {
        return "status=" + status + ", message='" + message + "\', ";
    }
}
