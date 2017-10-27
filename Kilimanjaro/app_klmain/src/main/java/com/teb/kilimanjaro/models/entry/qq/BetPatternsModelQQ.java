package com.teb.kilimanjaro.models.entry.qq;

import com.teb.kilimanjaro.models.entry.BaseJsonModel;

import java.util.List;

/**
 * Created by toughegg on 16/8/2.
 * QQ群玩法   返回的模式数据
 */
public class BetPatternsModelQQ extends BaseJsonModel {

    private List<BetPatternsData> data;

    public List<BetPatternsData> getData() {
        return data;
    }

    public void setData(List<BetPatternsData> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "BetPatternsModelQQ{" +
                toMyString() +
                "data=" + data +
                '}';
    }

    public static class BetPatternsData {
        private int gameId;// 在保存上次投注的时候需要使用

        public int getGameId() {
            return gameId;
        }

        public void setGameId(int gameId) {
            this.gameId = gameId;
        }

        private int id;
        private String patternName;// 模式名字   “压单”
        private String includeNum;// 模式包含的球   "1,3,5,7,9,11,13,15,17,19,21,23,25,27"
//        private float patternOdds;
        private String code;// 模式Code

        private boolean isSelect;

        public boolean isSelect() {
            return isSelect;
        }

        public void setSelect(boolean select) {
            isSelect = select;
        }

        public String[] getNums() {
            if (includeNum == null || includeNum.equals("")) {
                return null;
            }
            return includeNum.split(",");
        }

        public String getIncludeNum() {
            return includeNum;
        }

        public void setNums(String nums) {
            this.includeNum = nums;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getPatternName() {
            return patternName;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public void setPatternName(String patternName) {
            this.patternName = patternName;
        }
    }
}
