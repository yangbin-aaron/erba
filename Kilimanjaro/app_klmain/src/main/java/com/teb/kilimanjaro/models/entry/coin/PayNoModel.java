package com.teb.kilimanjaro.models.entry.coin;

import com.teb.kilimanjaro.models.entry.BaseJsonModel;

/**
 * Created by yangbin on 16/8/10.
 * 获取订单No返回的数据
 */
public class PayNoModel extends BaseJsonModel {

    private PayNoData data;

    public PayNoData getData() {
        return data;
    }

    public void setData(PayNoData data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "PayNoModel{" +
                toMyString() +
                "data=" + data +
                '}';
    }

    public static class PayNoData {
        private String payNo;

        public String getPayNo() {
            return payNo;
        }

        public void setPayNo(String payNo) {
            this.payNo = payNo;
        }

        @Override
        public String toString() {
            return "PayNoData{" +
                    "payNo='" + payNo + '\'' +
                    '}';
        }
    }
}
