package com.teb.kilimanjaro.models.entry.coin;

import com.teb.kilimanjaro.models.entry.BaseJsonModel;
import com.teb.kilimanjaro.utils.StringUtil;

import java.util.List;

/**
 * Created by yangbin on 16/8/18.
 */
public class WithdrawListModel extends BaseJsonModel {

    private List<WithdrawData> data;

    public List<WithdrawData> getData() {
        return data;
    }

    public void setData(List<WithdrawData> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "WithdrawListModel{" +
                toMyString() +
                "data=" + data +
                '}';
    }

    public static class WithdrawData {
        private String payNo;// 提现ID
        private String drawAmt;// 提现额度
        private int status;
        private String desc;// 状态
        private String createTime;

        public String getPayNo() {
            return payNo;
        }

        public void setPayNo(String payNo) {
            this.payNo = payNo;
        }

        public String getDrawAmt() {
            return drawAmt;
        }

        public void setDrawAmt(String drawAmt) {
            this.drawAmt = drawAmt;
        }

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

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        @Override
        public String toString() {
            return "WithdrawData{" +
                    "payNo='" + payNo + '\'' +
                    ", drawAmt='" + drawAmt + '\'' +
                    ", status=" + status +
                    ", desc='" + desc + '\'' +
                    ", createTime='" + createTime + '\'' +
                    '}';
        }
    }
}
