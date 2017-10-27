package com.teb.kilimanjaro.models.entry.mine;

import com.teb.kilimanjaro.models.entry.BaseJsonModel;

/**
 * Created by yangbin on 16/7/6.
 * 验证验证码返回的json数据
 */
public class CaptchaModel extends BaseJsonModel {

    private String captchaToken;

    public String getCaptchaToken() {
        return captchaToken;
    }

    public void setToken(String captchaToken) {
        this.captchaToken = captchaToken;
    }

    @Override
    public String toString() {
        return "CaptchaModel{" +
                toMyString() +
                "captchaToken='" + captchaToken + '\'' +
                '}';
    }
}
