package com.teb.kilimanjaro.models.entry.mine;

import com.google.gson.Gson;
import com.teb.kilimanjaro.AppPrefs;
import com.teb.kilimanjaro.models.entry.BaseJsonModel;

/**
 * Created by yangbin on 16/7/4.
 * 登录时返回的数据对象（包含判断是否登录的方法）---完成注册也是用该类
 */
public class LoginModel extends BaseJsonModel {

        private String token;
    private LoginData data;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LoginData getData() {
        return data;
    }

    public void setData(LoginData data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "LoginModel{" +
                toMyString() +
                ", token='" + token + '\'' +
                ", data=" + data +
                '}';
    }

    /**
     * 登录返回的data数据
     */
    public class LoginData {
        private String avatar;// 头像
        private long coin;// 积分
        private String createTime;// 2016-07-04T11:39:07+08:00
        private String email;// 邮箱
        private int gender;// 性别
        private String lastLoginIp;// 最后一次登录的IP
        private String nickName;// 昵称
        private String phone;// 手机号码
        private String userName;// 用户名

        public String getAvatar() {
            return avatar;
        }

        public long getCoin() {
            return coin;
        }

        public String getCreateTime() {
            return createTime;
        }

        public String getEmail() {
            return email;
        }

        public int getGender() {
            return gender;
        }

        public String getLastLoginIp() {
            return lastLoginIp;
        }

        public String getNickName() {
            return nickName;
        }

        public String getPhone() {
            return phone;
        }

        public String getUserName() {
            return userName;
        }

        @Override
        public String toString() {
            return "LoginData{" +
                    "avatar='" + avatar + '\'' +
                    ", coin='" + coin + '\'' +
                    ", createTime='" + createTime + '\'' +
                    ", email='" + email + '\'' +
                    ", gender=" + gender +
                    ", lastLoginIp='" + lastLoginIp + '\'' +
                    ", nickName='" + nickName + '\'' +
                    ", phone='" + phone + '\'' +
                    ", userName='" + userName + '\'' +
                    '}';
        }
    }

    // ------------------对外开放的方法--------------------
    
    /**
     * 是否已经登录
     *
     * @return
     */
    public static boolean isLogined() {
        return AppPrefs.getInstance().getUserJson() == null ? false : true;
    }

    public static LoginModel getUserInfo() {
        String userJson = AppPrefs.getInstance().getUserJson();
        if (userJson != null) {
            return new Gson().fromJson(userJson, LoginModel.class);
        }
        return null;
    }
}
