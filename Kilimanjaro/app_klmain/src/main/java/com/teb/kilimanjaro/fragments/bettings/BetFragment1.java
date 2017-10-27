package com.teb.kilimanjaro.fragments.bettings;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.teb.kilimanjaro.AppPrefs;
import com.teb.kilimanjaro.R;
import com.teb.kilimanjaro.activitys.hall.BettingActivity;
import com.teb.kilimanjaro.adapters.BetJSModelGridViewAdapter;
import com.teb.kilimanjaro.adapters.BetJSNumGridViewAdapter;
import com.teb.kilimanjaro.models.bean.BetMode;
import com.teb.kilimanjaro.models.entry.hall.OddsListModel;
import com.teb.kilimanjaro.utils.MyUtil;
import com.teb.kilimanjaro.utils.ScreenUtil;
import com.teb.kilimanjaro.utils.StringUtil;
import com.teb.kilimanjaro.views.dialogs.BetAllDialog;

import java.util.List;

/**
 * Created by yangbin on 16/7/13.
 * 投注界面
 */
public class BetFragment1 extends Fragment implements View.OnClickListener, BetJSModelGridViewAdapter.OnBetModeItemClickListener, BetJSNumGridViewAdapter.OnBetNumItemClickListener, MyUtil.OnTimesItemClickLisenter {

    private GridView mModelGV, mNumGV;
    private int mNumGVHeight = 0;
    private List<OddsListModel.OddsData> mOddsDataList;
    private BetJSNumGridViewAdapter mBetListtAdapter;// 号码的适配器
    private BetJSModelGridViewAdapter mModelAdapter;// 模式的适配器

    private TextView mLotteryHintTV;

    private TextView mBetTV;// 投注按钮
    private TextView mTotalBetCountTV;
    private int mTotalBetCount;// 总注数
    private TextView mGameCoinTV;

    private LinearLayout mShakeLL;// 摇一摇按钮

    public void setOddsDataList(List<OddsListModel.OddsData> oddsDataList) {
        if (mOddsDataList == null) {
            mOddsDataList = oddsDataList;
            if (mNumGVHeight == 0) {
                int hangCount = (mOddsDataList.size() % 7 == 0) ? mOddsDataList.size() / 7 : mOddsDataList.size() / 7 + 1;// 计算行数
                ViewGroup.LayoutParams params = mNumGV.getLayoutParams();
                mNumGVHeight = ScreenUtil.dip2px(getActivity(), 40) * hangCount;
                params.height = mNumGVHeight;
                mNumGV.setLayoutParams(params);
            }
        } else {
            for (int i = 0; i < oddsDataList.size(); i++) {
                if (!mOddsDataList.get(i).isBeted()) {
                    mOddsDataList.set(i, oddsDataList.get(i));
                }
            }
        }
        handlerBetCountTV(mOddsDataList, false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bet_1, null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViews();
    }

    private void initViews() {
        mModelGV = (GridView) getView().findViewById(R.id.gv_mode);
        List<BetMode> list = MyUtil.getBetModeListFronJson(StringUtil.getAssetsFileContent("modes"));
        int hangCount = (list.size() % 7 == 0) ? list.size() / 7 : list.size() / 7 + 1;// 计算行数
        ViewGroup.LayoutParams params = mModelGV.getLayoutParams();
        params.height = ScreenUtil.dip2px(getActivity(), 50) * hangCount;
        mModelGV.setLayoutParams(params);
        mModelAdapter = new BetJSModelGridViewAdapter(list);
        mModelGV.setAdapter(mModelAdapter);
        mModelAdapter.setOnBetModeItemClickListener(this);

        mNumGV = (GridView) getView().findViewById(R.id.gv_nums);
        mBetListtAdapter = new BetJSNumGridViewAdapter();
        mNumGV.setAdapter(mBetListtAdapter);
        mBetListtAdapter.setOnBetNumItemClickListener(this);

        mLotteryHintTV = (TextView) getView().findViewById(R.id.tv_hint);

        // 倍数按钮
        MyUtil.setOnTimesItemClickLisenter(getView(), this);

        mBetTV = (TextView) getView().findViewById(R.id.btn_betting);
        mBetTV.setOnClickListener(this);

        mTotalBetCountTV = (TextView) getView().findViewById(R.id.tv_total_bet);
        mGameCoinTV = (TextView) getView().findViewById(R.id.tv_amount);

        mShakeLL = (LinearLayout) getView().findViewById(R.id.ll_shake);
        mShakeLL.setOnClickListener(this);
    }

    /**
     * 结束投注
     */
    public void finishBetBtn(int textResId) {
        mBetTV.setText(textResId);
        mBetTV.setEnabled(false);
        mShakeLL.setVisibility(View.INVISIBLE);
        mShakeLL.setEnabled(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_betting:// 投注
                ((BettingActivity) getActivity()).orderBet(mOddsDataList, mTotalBetCount);
                break;
            case R.id.ll_shake:// 摇一摇
                shake();
                break;
        }
    }

    /**
     * 摇一摇处理事件
     */
    private void shake() {
        int count = (int) (Math.random() * 14) + 1;
        String[] nums = MyUtil.getRandomNum(27, count);
        handlerBetCountTV(MyUtil.handlerOddsList(nums, mOddsDataList), false);
    }

    /**
     * 各种模式的回调事件
     *
     * @param modeCode
     * @param modeNums
     */
    @Override
    public void onBetModeItemClickListener(int modeCode, String[] modeNums) {
        handlerBetCountTV(MyUtil.handlerOddsList(modeNums, mOddsDataList), false);
    }

    /**
     * 点击数字回调
     *
     * @param position
     */
    @Override
    public void onBetNumItemClickListener(int position) {
        OddsListModel.OddsData data = mOddsDataList.get(position);
        if (data.isBeted()) return;// 已经投注过的，不允许修改
        data.setBetCoin(!data.isSelected() ? data.getDefaultBet() : 0);
        data.setSelected(data.getBetCoin() > 0);
        mOddsDataList.set(position, data);
        handlerBetCountTV(mOddsDataList, false);
    }

    /**
     * 倍数按钮的回调事件
     *
     * @param times 0 清空    -1 上期    -2 反选  -3 梭哈
     */
    @Override
    public void onTimesItemClickLisenter(float times) {
        if (times == -1) {
            ((BettingActivity) getActivity()).getLastMyBetList();
        } else if (times == -2) {
            handlerBetCountTV(MyUtil.handlerOddsListByTurn(mOddsDataList), true);
        } else if (times == -3) {
            final long[] temp = MyUtil.getBetCountAndTotalCoin(mOddsDataList);
            if (temp[0] > 0) {// 有投注
                new BetAllDialog(getActivity(), 0)
                        .listener(new BetAllDialog.OnDialogClickListener() {
                            @Override
                            public void onRightClicked(long coin) {
                                handlerBetCountTV(MyUtil.handlerOddsListByBetAll(mOddsDataList, coin, temp), false);
                            }
                        })
                        .show();
            }
        } else {
            if (mTotalBetCount > 0) {
                handlerBetCountTV(MyUtil.handlerOddsListByTimes(mOddsDataList, times), true);
            }
        }
    }

    /**
     * 计算总投注数量(内部使用)
     */
    private void handlerBetCountTV(List<OddsListModel.OddsData> tempList, boolean isNeedVerify) {
        long gameCoin = AppPrefs.getInstance().getGameLong();
        if (isNeedVerify) {// 是否需要验证(一般只在做乘法计算时计算)
            // 判断计算结果
            long[] temp = MyUtil.getBetCountAndTotalCoin(tempList);
            if (temp[1] < 0 || temp[1] > gameCoin) {// 小于0是指在计算结果过大的时候
                tempList = MyUtil.handlerOddsListByBetAll(tempList, gameCoin, temp);
            }
        }
        mOddsDataList = tempList;

        double[] tempAmount = MyUtil.getBetInfo(mOddsDataList);
        mTotalBetCount = (int) tempAmount[0];
        mTotalBetCountTV.setText(mTotalBetCount + "");
        mGameCoinTV.setText(gameCoin + "");
        if (mTotalBetCount == 0) {
            mLotteryHintTV.setVisibility(View.INVISIBLE);
        } else {
            mLotteryHintTV.setVisibility(View.VISIBLE);
            mLotteryHintTV.setText(getResources().getString(R.string.betting_expect_result)
                    + tempAmount[1]
                    + getResources().getString(R.string.betting_expect_result1)
                    + tempAmount[2]);
        }

        mBetListtAdapter.setList(mOddsDataList);
    }
}
