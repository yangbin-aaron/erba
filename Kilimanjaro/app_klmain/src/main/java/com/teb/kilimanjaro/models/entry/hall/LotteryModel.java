package com.teb.kilimanjaro.models.entry.hall;

import com.teb.kilimanjaro.models.entry.BaseJsonModel;
import com.teb.kilimanjaro.utils.StringUtil;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yangbin on 16/7/12.
 * 开奖数据列表
 */
public class LotteryModel extends BaseJsonModel {

    private List<LotteryData> data;

    public List<LotteryData> getData() {
        return data;
    }

    public void setData(List<LotteryData> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "LotteryModel{" +
                toMyString() +
                "data=" + data +
                '}';
    }

    public static class LotteryData implements Serializable {
        private int id;// 自增ID
        private int lotteryId;// 期数ID
        private int referLotteryId;// 第三方开奖官网对应期数
        private int gameId;// 游戏id
        private int status;// 开奖状态  0:未开奖 -1:无效 1:已开奖
        private String lotteryResult;// 中奖数字
        private String lotteryComment;// 中奖说明
        private String lotteryTime;// 开奖时间
        private int lotterySecond;// 剩余开奖秒数
        private String cumulative;// 奖池数量
        private int betNum;// 投注数量
        private String betCoin;// 投注金额
        private String pureProfit;// 盈亏数量
        private String incomeRate;// 盈亏比率

        // 本地使用
        private boolean isNoLottery = false;// 默认为已开奖的历史数据

        public boolean isNoLottery() {
            return isNoLottery;
        }

        public void setNoLottery(boolean noLottery) {
            isNoLottery = noLottery;
        }

        public int getLotteryId() {
            return lotteryId;
        }

        public void setLotteryId(int lotteryId) {
            this.lotteryId = lotteryId;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getGameId() {
            return gameId;
        }

        public void setGameId(int gameId) {
            this.gameId = gameId;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getLotteryResult() {
            return lotteryResult;
        }

        public void setLotteryResult(String lotteryResult) {
            this.lotteryResult = lotteryResult;
        }

        public String getLotteryComment() {
            return lotteryComment;
        }

        public void setLotteryComment(String lotteryComment) {
            this.lotteryComment = lotteryComment;
        }

        public String getLotteryTime() {
            return StringUtil.stringToString(StringUtil.DATA_FORMAT_YMDHMSZZZZZ, StringUtil.DATA_FORMAT_HM, lotteryTime);
        }

        public void setLotteryTime(String lotteryTime) {
            this.lotteryTime = lotteryTime;
        }

        public int getLotterySecond() {
            return lotterySecond;
        }

        public void setLotterySecond(int lotterySecond) {
            this.lotterySecond = lotterySecond;
        }

        public String getCumulative() {
            return cumulative;
        }

        public void setCumulative(String cumulative) {
            this.cumulative = cumulative;
        }

        public int getBetNum() {
            return betNum;
        }

        public void setBetNum(int betNum) {
            this.betNum = betNum;
        }

        public String getBetCoin() {
            return betCoin;
        }

        public void setBetCoin(String betCoin) {
            this.betCoin = betCoin;
        }

        public String getPureProfit() {
            return pureProfit;
        }

        public void setPureProfit(String revenue) {
            this.pureProfit = pureProfit;
        }

        public String getIncomeRate() {
            return incomeRate;
        }

        public void setIncomeRate(String incomeRate) {
            this.incomeRate = incomeRate;
        }

        public int getReferLotteryId() {
            return referLotteryId;
        }

        public void setReferLotteryId(int referLotteryId) {
            this.referLotteryId = referLotteryId;
        }

        @Override
        public String toString() {
            return "LotteryData{" +
                    "id=" + id +
                    ", lotteryId=" + lotteryId +
                    ", referLotteryId=" + referLotteryId +
                    ", status=" + status +
                    ", lotteryResult='" + lotteryResult + '\'' +
                    ", lotteryComment='" + lotteryComment + '\'' +
                    ", lotterySecond=" + lotterySecond +
                    ", cumulative='" + cumulative + '\'' +
                    ", betNum=" + betNum +
                    ", betCoin='" + betCoin + '\'' +
                    ", pureProfit='" + pureProfit + '\'' +
                    ", incomeRate='" + incomeRate + '\'' +
                    ", isNoLottery=" + isNoLottery +
                    '}';
        }
    }
}
