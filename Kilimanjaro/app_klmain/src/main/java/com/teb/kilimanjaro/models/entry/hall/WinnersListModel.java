package com.teb.kilimanjaro.models.entry.hall;

import com.teb.kilimanjaro.models.entry.BaseJsonModel;

import java.util.List;

/**
 * Created by yangbin on 16/7/15.
 * 中奖名单返回数据
 */
public class WinnersListModel extends BaseJsonModel {

    private List<WinnersListData> data;

    public List<WinnersListData> getData() {
        return data;
    }

    public void setData(List<WinnersListData> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "WinnersListModel{" +
                toMyString() +
                "data=" + data +
                '}';
    }

    public static class WinnersListData {
        private String userName;
        private int betCoin;
        private String revenue;

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public int getBetCoin() {
            return betCoin;
        }

        public void setBetCoin(int betCoin) {
            this.betCoin = betCoin;
        }

        public String getRevenue() {
            return revenue;
        }

        public void setRevenue(String revenue) {
            this.revenue = revenue;
        }

        @Override
        public String toString() {
            return "WinnersListData{" +
                    "userName='" + userName + '\'' +
                    ", betCoin=" + betCoin +
                    ", revenue='" + revenue + '\'' +
                    '}';
        }
    }
}
