package com.teb.kilimanjaro.models.entry.hall;

import com.teb.kilimanjaro.models.entry.BaseJsonModel;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yangbin on 16/7/14.
 * 28个号码   以及 倍率  返回数据
 */
public class OddsListModel extends BaseJsonModel {

    private List<OddsData> data;

    public List<OddsData> getData() {
        return data;
    }

    public void setData(List<OddsData> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "OddsListModel{" +
                toMyString() +
                "data=" + data +
                '}';
    }

    public static class OddsData implements Serializable {
        private int gameId;
        private int lotteryNumber;
        private double odds;
        private long defaultBet;// 默认下注数量(用于极速模式)
        private String revenue;// 收益

        private boolean isBeted;// 之前投注过的不能修改
        private long betCoin;// 投注的金额

        private boolean isSelected = false;
        private boolean focus = false;// 是否获取了焦点（在普通投注里面使用）

        public boolean isFocus() {
            return focus;
        }

        public void setFocus(boolean focus) {
            this.focus = focus;
        }

        public int getGameId() {
            return gameId;
        }

        public void setGameId(int gameId) {
            this.gameId = gameId;
        }

        public int getLotteryNumber() {
            return lotteryNumber;
        }

        public void setLotteryNumber(int lotteryNumber) {
            this.lotteryNumber = lotteryNumber;
        }

        public double getOdds() {
            return odds;
        }

        public void setOdds(double odds) {
            this.odds = odds;
        }

        public long getDefaultBet() {
            return defaultBet;
        }

        public void setDefaultBet(long defaultBet) {
            this.defaultBet = defaultBet;
        }

        public String getRevenue() {
            return revenue;
        }

        public void setRevenue(String revenue) {
            this.revenue = revenue;
        }

        public long getBetCoin() {
            return betCoin;
        }

        public void setBetCoin(long betCoin) {
            this.betCoin = betCoin;
        }

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }

        public boolean isBeted() {
            return isBeted;
        }

        public void setBeted(boolean beted) {
            isBeted = beted;
        }

        @Override
        public String toString() {
            return "OddsData{" +
                    "gameId=" + gameId +
                    ", lotteryNumber=" + lotteryNumber +
                    ", odds=" + odds +
                    ", defaultBet=" + defaultBet +
                    ", revenue='" + revenue + '\'' +
                    ", betCoin=" + betCoin +
                    ", isSelected=" + isSelected +
                    ", isBeted=" + isBeted +
                    '}';
        }
    }
}
