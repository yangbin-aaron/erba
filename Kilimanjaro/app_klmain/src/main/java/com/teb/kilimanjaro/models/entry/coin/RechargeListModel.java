package com.teb.kilimanjaro.models.entry.coin;

import com.teb.kilimanjaro.models.entry.BaseJsonModel;
import com.teb.kilimanjaro.utils.StringUtil;

import java.util.List;

/**
 * Created by yangbin on 16/8/12.
 * 账单明细
 */
public class RechargeListModel extends BaseJsonModel {

    private List<RechargeData> data;

    public List<RechargeData> getData() {
        return data;
    }

    public void setData(List<RechargeData> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "RechargeListModel{" +
                toMyString() +
                "data=" + data +
                '}';
    }

    public static class RechargeData {
        private String desc;
        private String coin;
        private String createTime;

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getCoin() {
            return coin;
        }

        public void setCoin(String coin) {
            this.coin = coin;
        }

        public String getCreateTime() {
            return StringUtil.stringToString(StringUtil.DATA_FORMAT_YMDHMSZZZZZ, StringUtil.DATA_FORMAT_YMDHMS, createTime);
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        @Override
        public String toString() {
            return "Recharge{" +
                    "desc='" + desc + '\'' +
                    ", coin='" + coin + '\'' +
                    ", createTime='" + createTime + '\'' +
                    '}';
        }
    }
}
