package com.teb.kilimanjaro.models.entry.hall;

import com.teb.kilimanjaro.models.entry.BaseJsonModel;

import java.util.List;

/**
 * Created by yangbin on 16/7/22.
 * 按期亏盈  返回数据
 */
public class RevenueByPeriodsModel extends BaseJsonModel {

    private List<RevenueByPeriodsData> data;

    public List<RevenueByPeriodsData> getData() {
        return data;
    }

    @Override
    public String toString() {
        return "RevenueByPeriodsModel{" +
                toMyString() +
                "data=" + data +
                '}';
    }

    public static class RevenueByPeriodsData {
        private int lotteryId; //Integer 期数
        private String lotteryNumber; //String 中奖号码
        private long betCoin; //BigDecimal 下注额
        private long revenue; //BigDecimal 收益

        public int getLotteryId() {
            return lotteryId;
        }

        public String getLotteryNumber() {
            return lotteryNumber;
        }

        public long getBetCoin() {
            return betCoin;
        }

        public long getRevenue() {
            return revenue;
        }

        @Override
        public String toString() {
            return "RevenueByPeriodsData{" +
                    "lotteryId=" + lotteryId +
                    ", lotteryNumber='" + lotteryNumber + '\'' +
                    ", betCoin=" + betCoin +
                    ", revenue=" + revenue +
                    '}';
        }
    }
}
