package com.teb.kilimanjaro.models.entry.coin;

import com.teb.kilimanjaro.models.entry.BaseJsonModel;

import java.io.Serializable;

/**
 * Created by yangbin on 16/8/18.
 * 提现预备信息获取
 */
public class PrepareInfoModel extends BaseJsonModel {

    private PrepareInfoData data;

    public PrepareInfoData getData() {
        return data;
    }

    public void setData(PrepareInfoData data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "PrepareInfoModel{" +
                toMyString() +
                "data=" + data +
                '}';
    }

    public static class PrepareInfoData implements Serializable{
        private String bankNum;// 银行卡号
        private String bindPhone;// 银行预留电话号码
        private long canDrawAmt;// 可提现金额

        public String getBankNum() {
            return bankNum;
        }

        public void setBankNum(String bankNum) {
            this.bankNum = bankNum;
        }

        public String getBindPhone() {
            return bindPhone;
        }

        public void setBindPhone(String bindPhone) {
            this.bindPhone = bindPhone;
        }

        public long getCanDrawAmt() {
            return canDrawAmt;
        }

        public void setCanDrawAmt(long canDrawAmt) {
            this.canDrawAmt = canDrawAmt;
        }

        @Override
        public String toString() {
            return "PrepareInfoData{" +
                    "bankNum='" + bankNum + '\'' +
                    ", bindPhone='" + bindPhone + '\'' +
                    ", canDrawAmt=" + canDrawAmt +
                    '}';
        }
    }
}
