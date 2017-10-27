package com.teb.kilimanjaro.fragments.bettings;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.teb.kilimanjaro.AppPrefs;
import com.teb.kilimanjaro.R;
import com.teb.kilimanjaro.activitys.hall.BettingActivity;
import com.teb.kilimanjaro.adapters.BetListtAdapter;
import com.teb.kilimanjaro.models.entry.hall.OddsListModel;
import com.teb.kilimanjaro.utils.MyUtil;
import com.teb.kilimanjaro.views.dialogs.BetAllDialog;

import java.util.List;

/**
 * Created by yangbin on 16/7/13.
 * 投注界面
 */
public class BetFragment0 extends Fragment
        implements View.OnClickListener,
        BetListtAdapter.OnBetCoinItemClickListener,
        MyUtil.OnKeyboardItemClickLisenter,
        MyUtil.OnTimesItemClickLisenter,
        MyUtil.OnOtherModeItemClickLisenter {

    // 投注界面ListView
    private ListView mListView;
    private List<OddsListModel.OddsData> mOddsDataList;
    private BetListtAdapter mBetListtAdapter;

    private LinearLayout mMyKeyboardLL;
    private int mItemIndex = 0;

    // Bottom
    private TextView mTimesTV, mModelTV, mBetTV;// 倍数   模式   投注
    private ImageView mTimesIV, mModelIV;// 倍数和模式下面的标记
    private RelativeLayout mBottomRL;// 最下面的自定义键盘
    private int mKeyState;// 自定义键盘打开状态    0关闭    1打开倍数    2打开模式

    private TextView mTotalBetCountTV;
    private int mTotalBetCount;// 总注数
    private TextView mGameCoinTV;

    private Handler mHandler = new Handler();
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            mListView.setSelection(mItemIndex);// 滚到可见状态
        }
    };

    public void setOddsDataList(List<OddsListModel.OddsData> oddsDataList) {
        if (mOddsDataList == null) {
            mOddsDataList = oddsDataList;
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
        return inflater.inflate(R.layout.fragment_bet_0, null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViews();
    }

    private void initViews() {
        mMyKeyboardLL = (LinearLayout) getView().findViewById(R.id.ll_my_keyboard);

        // ******投注界面ListView
        mListView = (ListView) getView().findViewById(R.id.lv_bet);
        mListView.addHeaderView(initHeaderView());

        mBetListtAdapter = new BetListtAdapter();
        mListView.setAdapter(mBetListtAdapter);
        mBetListtAdapter.setOnBetCoinItemClickListener(this);

        // ******bottom
        mBottomRL = (RelativeLayout) getView().findViewById(R.id.rl_bottom);
        mTimesTV = (TextView) getView().findViewById(R.id.btn_times);
        mTimesTV.setOnClickListener(this);
        mModelTV = (TextView) getView().findViewById(R.id.btn_mode);
        mModelTV.setOnClickListener(this);
        mBetTV = (TextView) getView().findViewById(R.id.btn_betting);
        mBetTV.setOnClickListener(this);
        mTimesIV = (ImageView) getView().findViewById(R.id.iv_times_state);
        mModelIV = (ImageView) getView().findViewById(R.id.iv_mode_state);

        // ******键盘
        MyUtil.setOnKeyboardItemClickLisenter(getView(), this);

        mTotalBetCountTV = (TextView) getView().findViewById(R.id.tv_total_bet);
        mGameCoinTV = (TextView) getView().findViewById(R.id.tv_amount);

        // 倍数按钮
        MyUtil.setOnTimesItemClickLisenter(getView(), this);

        // 其他模式按钮
        MyUtil.setOnOtherModeItemClickLisenter(getView(), this);
    }

    /**
     * 投注界面头部倍数选择
     *
     * @return
     */
    private View initHeaderView() {
        LinearLayout linearLayout = new LinearLayout(getActivity());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        AbsListView.LayoutParams params = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT);
        linearLayout.setLayoutParams(params);
        linearLayout.setBackgroundResource(R.color.gray_line);

        View view0 = LayoutInflater.from(getActivity()).inflate(R.layout.addview_bet_times_header, null);
        linearLayout.addView(view0);

        View view1 = LayoutInflater.from(getActivity()).inflate(R.layout.addview_bet_times_other_header, null);
        linearLayout.addView(view1, 1);
        return linearLayout;
    }

    private void changeKey() {
        switch (mKeyState) {
            case 0:
                mBottomRL.setVisibility(View.GONE);
                mTimesIV.setVisibility(View.GONE);
                mModelIV.setVisibility(View.GONE);
                break;
            case 1:
                mBottomRL.setVisibility(View.VISIBLE);
                getView().findViewById(R.id.zuo).setVisibility(View.VISIBLE);
                getView().findViewById(R.id.you).setVisibility(View.GONE);
                mTimesIV.setVisibility(View.VISIBLE);
                mModelIV.setVisibility(View.GONE);
                break;
            case 2:
                mBottomRL.setVisibility(View.VISIBLE);
                getView().findViewById(R.id.zuo).setVisibility(View.GONE);
                getView().findViewById(R.id.you).setVisibility(View.VISIBLE);
                mTimesIV.setVisibility(View.GONE);
                mModelIV.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.btn_times:// 倍数按钮
//                mKeyState = (mKeyState == 2 || mKeyState == 0) ? 1 : 0;
//                changeKey();
//                break;
//            case R.id.btn_mode:// 模式按钮
//                mKeyState = (mKeyState == 1 || mKeyState == 0) ? 2 : 0;
//                changeKey();
//                break;
            case R.id.btn_betting:// 投注按钮
                ((BettingActivity) getActivity()).orderBet(mOddsDataList, mTotalBetCount);
                break;
        }
    }

    /**
     * mListView 的监听回调    （输入框）
     */
    @Override
    public void onBetCoinItemClickListener(int position) {
        OddsListModel.OddsData data = mOddsDataList.get(position);
        if (data.isBeted()) return;// 已经投注过的，不允许修改

        mMyKeyboardLL.setVisibility(View.VISIBLE);
        mHandler.postDelayed(mRunnable, 10);

        if (mItemIndex != position) {
            OddsListModel.OddsData data1 = mOddsDataList.get(mItemIndex);
            data1.setFocus(false);
            mOddsDataList.set(mItemIndex, data1);
        }
        data.setFocus(true);
        mOddsDataList.set(position, data);
        mBetListtAdapter.setList(mOddsDataList);

        mItemIndex = position;
    }

    /**
     * mListView 的监听回调    （倍数）
     *
     * @param position
     * @param times
     */
    @Override
    public void onTimesItemClickListener(int position, float times) {
        OddsListModel.OddsData data = mOddsDataList.get(position);
        if (data.isBeted()) return;// 已经投注过的，不允许修改
        data.setBetCoin((long) (data.getBetCoin() * times));
        data.setSelected(data.getBetCoin() > 0);
        mOddsDataList.set(position, data);
        handlerBetCountTV(mOddsDataList, false);
    }

    /**
     * 号码点击事件
     *
     * @param position
     */
    @Override
    public void onNumItemClickListener(int position) {
        OddsListModel.OddsData data = mOddsDataList.get(position);
        if (data.isBeted()) return;// 已经投注过的，不允许修改
        data.setBetCoin(!data.isSelected() ? data.getDefaultBet() : 0);
        data.setSelected(data.getBetCoin() > 0);
        mOddsDataList.set(position, data);
        handlerBetCountTV(mOddsDataList, false);
    }

    /**
     * 数字键盘的监听
     *
     * @param position 10 关闭键盘   11  删除
     */
    @Override
    public void onKeyboardItemClickLisenter(int position) {
        OddsListModel.OddsData data = mOddsDataList.get(mItemIndex);
        long text = data.getBetCoin();
        if (position == 10) {
            mMyKeyboardLL.setVisibility(View.GONE);
            data.setFocus(false);
        } else if (position == 11) {
            text = text / 10;
            data.setBetCoin(text);
            data.setSelected(text > 0);
        } else {
            text = text * 10 + position;
            data.setBetCoin(text);
            data.setSelected(text > 0);
        }
        mOddsDataList.set(mItemIndex, data);
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
     * 其他模式按钮回调事件
     *
     * @param modeNums
     */
    @Override
    public void onOtherModeItemClickLisenter(String[] modeNums) {
        List<OddsListModel.OddsData> tempList = MyUtil.handlerOddsList(modeNums, mOddsDataList);
        handlerBetCountTV(tempList, false);
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
        mBetListtAdapter.setList(mOddsDataList);
    }
}
