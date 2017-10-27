package com.teb.kilimanjaro.models.entry.qq;

import com.teb.kilimanjaro.models.entry.BaseJsonModel;
import com.teb.kilimanjaro.utils.StringUtil;

import java.util.List;

/**
 * Created by toughegg on 16/8/2.
 * 投注列表数据返回
 */
public class BettingListModelQQ extends BaseJsonModel {

    private List<BettingListData> data;

    public List<BettingListData> getData() {
        return data;
    }

    public void setData(List<BettingListData> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "BettingListModelQQ{" +
                toMyString() +
                "data=" + data +
                '}';
    }

    public static class BettingListData {
        private String orderNo;// 订单ID
        private String betCoin;// 订单金额
        private String patternName;// 下单模式
        private String betNum;// 号码
        private String revenue;
        private String createTime;

        public String getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(String orderNo) {
            this.orderNo = orderNo;
        }

        public String getBetCoin() {
            return betCoin;
        }

        public void setBetCoin(String betCoin) {
            this.betCoin = betCoin;
        }

        public String getPatternName() {
            return patternName;
        }

        public void setPatternName(String patternName) {
            this.patternName = patternName;
        }

        public String getBetNum() {
            return betNum;
        }

        public void setBetNum(String betNum) {
            this.betNum = betNum;
        }

        public String getRevenue() {
            return revenue;
        }

        public void setRevenue(String revenue) {
            this.revenue = revenue;
        }

        public String getCreateTime() {
            return StringUtil.stringToString(StringUtil.DATA_FORMAT_YMDHMSZZZZZ, StringUtil.DATA_FORMAT_YMDHMS, createTime);
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        @Override
        public String toString() {
            return "BettingListData{" +
                    "orderNo='" + orderNo + '\'' +
                    ", betCoin='" + betCoin + '\'' +
                    ", patternName='" + patternName + '\'' +
                    ", betNum='" + betNum + '\'' +
                    ", revenue=" + revenue +
                    ", createTime='" + createTime + '\'' +
                    '}';
        }
    }
}
