package com.teb.kilimanjaro.models.bean;

/**
 * Created by yangbin on 16/7/27.
 * 自动投注的参数封装对象
 */
public class BetAutoModel {

    // 开始参数
    private ModeAutoList.ModeAutoData mModeAutoDataOfStart;// 投注模式数据
    private int mStartIssue;// 开始期号

    // 结束参数
    private int mAutoCount;// 自动期数
    private long mMaxCoin, mMinCoin;// 余额达到max时，余额不足min时
    private boolean mIsContinueAfterWin;// 中奖后是否继续投注

    public ModeAutoList.ModeAutoData getModeAutoDataOfStart() {
        return mModeAutoDataOfStart;
    }

    public void setModeAutoDataOfStart(ModeAutoList.ModeAutoData modeAutoDataOfStart) {
        mModeAutoDataOfStart = modeAutoDataOfStart;
    }

    public int getStartIssue() {
        return mStartIssue;
    }

    public void setStartIssue(int startIssue) {
        mStartIssue = startIssue;
    }

    public int getAutoCount() {
        return mAutoCount;
    }

    public void setAutoCount(int autoCount) {
        mAutoCount = autoCount;
    }

    public long getMaxCoin() {
        return mMaxCoin;
    }

    public void setMaxCoin(long maxCoin) {
        mMaxCoin = maxCoin;
    }

    public long getMinCoin() {
        return mMinCoin;
    }

    public void setMinCoin(long minCoin) {
        mMinCoin = minCoin;
    }

    public boolean isContinueAfterWin() {
        return mIsContinueAfterWin;
    }

    public void setContinueAfterWin(boolean continueAfterWin) {
        mIsContinueAfterWin = continueAfterWin;
    }

    @Override
    public String toString() {
        return "BetAutoModel{" +
                "mModeAutoDataOfStart=" + mModeAutoDataOfStart +
                ", mStartIssue=" + mStartIssue +
                ", mAutoCount=" + mAutoCount +
                ", mMaxCoin=" + mMaxCoin +
                ", mMinCoin=" + mMinCoin +
                ", mIsContinueAfterWin=" + mIsContinueAfterWin +
                '}';
    }
}
