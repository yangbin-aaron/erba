package com.teb.kilimanjaro.models.entry.mine;

import com.teb.kilimanjaro.models.entry.BaseJsonModel;

/**
 * Created by yangbin on 16/8/22.
 */
public class VersionStatusModel extends BaseJsonModel {

    private VersionStatusData data;

    public VersionStatusData getData() {
        return data;
    }

    public void setData(VersionStatusData data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "VersionStatusModel{" +
                toMyString() +
                "data=" + data +
                '}';
    }

    public static class VersionStatusData {
        private int versionStatus;
        private String url;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getVersionStatus() {
            return versionStatus;
        }

        public void setVersionStatus(int versionStatus) {
            this.versionStatus = versionStatus;
        }

        @Override
        public String toString() {
            return "VersionStatusData{" +
                    "versionStatus=" + versionStatus +
                    "url=" + url +
                    '}';
        }
    }
}
