package com.teb.kilimanjaro.models.entry.coin;

import com.teb.kilimanjaro.models.entry.BaseJsonModel;
import com.teb.kilimanjaro.utils.StringUtil;

import java.util.List;

/**
 * Created by yangbin on 16/8/18.
 */
public class WithdrawDetailModel extends BaseJsonModel {

    private WithdrawDetailData data;

    public WithdrawDetailData getData() {
        return data;
    }

    public void setData(WithdrawDetailData data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "WithdrawDetailModel{" +
                toMyString() +
                "data=" + data +
                '}';
    }

    public static class WithdrawDetailData {
        private int status; //String 提现状态(-1:提现申请 -2:申请通过  -3:申请拒绝  -4:提现成功  -5:提现失败  -6:提现撤销 )
        private String failReason;// String  失败理由  (只有当状态为 -3和-5 才有该字段)
        private List<RecordsData> records;

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getFailReason() {
            return failReason;
        }

        public void setFailReason(String failReason) {
            this.failReason = failReason;
        }

        public List<RecordsData> getRecords() {
            return records;
        }

        public void setRecords(List<RecordsData> records) {
            this.records = records;
        }

        @Override
        public String toString() {
            return "WithdrawDetailData{" +
                    "status=" + status +
                    ", failReason='" + failReason + '\'' +
                    ", records=" + records +
                    '}';
        }
    }

    public static class RecordsData {
        private String desc;
        private String createTime;

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getCreateTime() {
            return StringUtil.stringToString(StringUtil.DATA_FORMAT_YMDHMSZZZZZ, StringUtil.DATA_FORMAT_YMDHMS, createTime);
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        @Override
        public String toString() {
            return "RecordsData{" +
                    "desc='" + desc + '\'' +
                    ", createTime='" + createTime + '\'' +
                    '}';
        }
    }
}
