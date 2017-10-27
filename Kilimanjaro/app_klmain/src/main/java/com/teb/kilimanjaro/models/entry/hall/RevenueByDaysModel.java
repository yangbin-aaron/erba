package com.teb.kilimanjaro.models.entry.hall;

import com.teb.kilimanjaro.models.entry.BaseJsonModel;

import java.util.List;

/**
 * Created by yangbin on 16/7/22.
 * 按日亏盈  返回数据
 */
public class RevenueByDaysModel extends BaseJsonModel {

    private List<RevenueByDaysData> data;

    public List<RevenueByDaysData> getData() {
        return data;
    }

    @Override
    public String toString() {
        return "RevenueByDaysModel{" +
                toMyString() +
                "data=" + data +
                '}';
    }

    public static class RevenueByDaysData {
        private String date; //"2016-07-18" , //String 日期
        private long revenue;// 235641 //BigDecimal 当日收益

        public String getDate() {
            return date;
        }

        public long getRevenue() {
            return revenue;
        }

        @Override
        public String toString() {
            return "RevenueByDaysData{" +
                    "date='" + date + '\'' +
                    ", revenue=" + revenue +
                    '}';
        }
    }
}
