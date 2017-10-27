package com.teb.kilimanjaro.models.entry.hall;

import com.teb.kilimanjaro.models.entry.BaseJsonModel;

import java.util.List;

/**
 * Created by yangbin on 16/7/5.
 * 获取游戏列表时返回的数据对象
 */
public class GameListModel extends BaseJsonModel {
    private List<GameListData> data;

    public List<GameListData> getData() {
        return data;
    }

    public void setData(List<GameListData> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "GameListModel{" +
                toMyString() +
                "data=" + data +
                '}';
    }

    public static class GameListData {

        /**
         * 游戏是否开通
         *
         * @return
         */
        public boolean isVisible() {
            return visible == 1;
        }

        /**
         * 是否是模拟场
         *
         * @return
         */
        public boolean isSimulate() {
            return isSimulate == 1;
        }

        private int id;
        private String gameName;
        private String icon;
        private int interval;// 访问数据间隔时间(Service用到)
        private int visible;// 游戏是否开通，1开通   0
        private String createTime;
        private int stopBetSecond;// 停止投注时间
        private String capCoin;// 下注封顶金额
        private String referName;// 数据来源网站
        private String referUrl;
        private String descUrl;// 玩法介绍
        private int isSimulate;// 是否模拟场

        public String getDescUrl() {
            return descUrl;
        }

        public void setDescUrl(String descUrl) {
            this.descUrl = descUrl;
        }

        public String getReferName() {
            return referName;
        }

        public void setReferName(String referName) {
            this.referName = referName;
        }

        public String getReferUrl() {
            return referUrl;
        }

        public void setReferUrl(String referUrl) {
            this.referUrl = referUrl;
        }

        public long getCapCoin() {
            long capCoin = 0;
            if (this.capCoin != null && !"".equals(this.capCoin)) {
                capCoin = Long.parseLong(this.capCoin);
            }
            return capCoin;
        }

        public void setCapCoin(String capCoin) {
            this.capCoin = capCoin;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public void setStopBetSecond(int stopBetSecond) {
            this.stopBetSecond = stopBetSecond;
        }

        public int getStopBetSecond() {
            return stopBetSecond;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getGameName() {
            return gameName;
        }

        public void setGameName(String gameName) {
            this.gameName = gameName;
        }

        public int getInterval() {
            return interval;
        }

        public void setInterval(int interval) {
            this.interval = interval;
        }

        public int getVisible() {
            return visible;
        }

        public void setVisible(int visible) {
            this.visible = visible;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        @Override
        public String toString() {
            return "GameListData{" +
                    "id=" + id +
                    ", gameName='" + gameName + '\'' +
                    ", icon='" + icon + '\'' +
                    ", interval=" + interval +
                    ", visible=" + visible +
                    ", createTime='" + createTime + '\'' +
                    ", stopBetSecond=" + stopBetSecond +
                    ", capCoin='" + capCoin + '\'' +
                    ", referName='" + referName + '\'' +
                    ", referUrl='" + referUrl + '\'' +
                    ", descUrl='" + descUrl + '\'' +
                    '}';
        }
    }
}
