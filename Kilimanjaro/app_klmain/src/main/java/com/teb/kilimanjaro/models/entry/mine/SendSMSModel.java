package com.teb.kilimanjaro.models.entry.mine;

import com.teb.kilimanjaro.models.entry.BaseJsonModel;

/**
 * Created by yangbin on 16/7/6.
 * 发送验证码短信 json返回数据
 */
public class SendSMSModel extends BaseJsonModel {

    private SendSMSData data;

    public SendSMSData getData() {
        return data;
    }

    public void setData(SendSMSData data) {
        this.data = data;
    }

    public class SendSMSData {
        private int expired;// 有效时常
        private String phone;

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public int getExpired() {
            return expired;
        }

        public void setExpired(int expired) {
            this.expired = expired;
        }

        @Override
        public String toString() {
            return "SendSMSData{" +
                    "expired=" + expired +
                    ", phone='" + phone + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "SendSMSModel{" +
                toMyString() +
                "data=" + data +
                '}';
    }
}
