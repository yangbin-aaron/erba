package com.teb.kilimanjaro.models.bean;

import com.teb.kilimanjaro.models.entry.hall.OddsListModel;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yangbin on 16/7/26.
 * 自动投注的模式列表
 */
public class ModeAutoList {

    public ModeAutoList() {
    }

    public ModeAutoList(List<ModeAutoData> data) {
        this.data = data;
    }

    private List<ModeAutoData> data;

    public List<ModeAutoData> getData() {
        return data;
    }

    public void setData(List<ModeAutoData> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ModeAutoList{" +
                "data=" + data +
                '}';
    }

    public static class ModeAutoData implements Serializable {
        private int id;
        private String name;
        private long totalCount;
        private List<OddsListModel.OddsData> list;

        public long getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(long totalCount) {
            this.totalCount = totalCount;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<OddsListModel.OddsData> getList() {
            return list;
        }

        public void setList(List<OddsListModel.OddsData> list) {
            this.list = list;
        }

        @Override
        public String toString() {
            return "ModeAutoData{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", totalCount=" + totalCount +
                    ", list=" + list +
                    '}';
        }
    }
}
