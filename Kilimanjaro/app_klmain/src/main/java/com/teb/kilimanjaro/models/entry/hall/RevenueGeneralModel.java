package com.teb.kilimanjaro.models.entry.hall;

import com.teb.kilimanjaro.models.entry.BaseJsonModel;

/**
 * Created by yangbin on 16/7/22.
 * 亏盈概览  返回数据
 */
public class RevenueGeneralModel extends BaseJsonModel {

    private RevenueGeneralData data;

    public RevenueGeneralData getData() {
        return data;
    }

    public void setData(RevenueGeneralData data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "RevenueGeneralModel{" +
                toMyString() +
                "data=" + data +
                '}';
    }

    public static class RevenueGeneralData {
        private int periods; //Integer 今日参与期数
        private long todayRevenue; //BigDecimal 今日盈亏
        private String victoryRate; //String 胜率
        private long yesterdayRevenue; //BigDecimal 昨日盈亏
        private long weekRevenue; //BigDecimal 本周盈亏
        private long lastWeekRevenue; //BigDecimal 上周盈亏
        private long monthRevenue; //BigDecimal 本月盈亏
        private long lastMonthRevenue; //BigDecimal 上月盈亏
        private long yearRevenue; //BigDecimal 今年盈亏
        private long lastYearRevenue; //BigDecimal 去年盈亏

        public int getPeriods() {
            return periods;
        }

        public long getTodayRevenue() {
            return todayRevenue;
        }

        public String getVictoryRate() {
            return victoryRate;
        }

        public long getYesterdayRevenue() {
            return yesterdayRevenue;
        }

        public long getWeekRevenue() {
            return weekRevenue;
        }

        public long getLastWeekRevenue() {
            return lastWeekRevenue;
        }

        public long getMonthRevenue() {
            return monthRevenue;
        }

        public long getLastMonthRevenue() {
            return lastMonthRevenue;
        }

        public long getYearRevenue() {
            return yearRevenue;
        }

        public long getLastYearRevenue() {
            return lastYearRevenue;
        }

        @Override
        public String toString() {
            return "RevenueGeneralData{" +
                    "periods=" + periods +
                    ", todayRevenue=" + todayRevenue +
                    ", victoryRate='" + victoryRate + '\'' +
                    ", yesterdayRevenue=" + yesterdayRevenue +
                    ", weekRevenue=" + weekRevenue +
                    ", lastWeekRevenue=" + lastWeekRevenue +
                    ", monthRevenue=" + monthRevenue +
                    ", lastMonthRevenue=" + lastMonthRevenue +
                    ", yearRevenue=" + yearRevenue +
                    ", lastYearRevenue=" + lastYearRevenue +
                    '}';
        }
    }
}
