package com.teb.kilimanjaro.models.entry.coin;

import com.teb.kilimanjaro.models.entry.BaseJsonModel;

/**
 * Created by yangbin on 16/7/21.
 * 获取游戏积分返回数据
 */
public class GameCoinModel extends BaseJsonModel {

    private GameCoinData data;

    public GameCoinData getData() {
        return data;
    }

    public void setData(GameCoinData data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "GameCoinModel{" +
                toMyString() +
                "data=" + data +
                '}';
    }

    public static class GameCoinData {
        private long coin;// 金豆
        private long score;// 积分

        public long getCoin() {
            return coin;
        }

        public void setCoin(long coin) {
            this.coin = coin;
        }

        public long getScore() {
            return score;
        }

        public void setScore(long score) {
            this.score = score;
        }

        @Override
        public String toString() {
            return "GameCoinData{" +
                    "coin=" + coin +
                    ", score=" + score +
                    '}';
        }
    }
}
