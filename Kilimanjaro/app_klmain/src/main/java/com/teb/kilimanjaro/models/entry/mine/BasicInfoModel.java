package com.teb.kilimanjaro.models.entry.mine;

import android.text.TextUtils;

import com.teb.kilimanjaro.App;
import com.teb.kilimanjaro.R;
import com.teb.kilimanjaro.models.entry.BaseJsonModel;

/**
 * Created by yangbin on 16/9/14.
 * 客服信息
 */
public class BasicInfoModel extends BaseJsonModel {
    private BasicInfoData data;

    public BasicInfoData getData() {
        return data;
    }

    public void setData(BasicInfoData data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "BasicInfoModel{" +
                toMyString() +
                "data=" + data +
                '}';
    }

    public static class BasicInfoData {
        private String phone;// 客服电话
        private String qq;// 客服QQ

        public boolean hasPhone() {
            return !TextUtils.isEmpty(phone);
        }

        public boolean hasQQ() {
            return !TextUtils.isEmpty(qq);
        }

        public String getPhone() {
            return hasPhone() ? phone : App.getAppContext().getString(R.string.call_phone_default);
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getQq() {
            return hasQQ() ? qq : App.getAppContext().getString(R.string.kefu_qq_num_default);
        }

        public void setQq(String qq) {
            this.qq = qq;
        }

        @Override
        public String toString() {
            return "BasicInfoData{" +
                    "phone='" + phone + '\'' +
                    ", qq='" + qq + '\'' +
                    '}';
        }
    }
}
