package com.teb.kilimanjaro.activitys.hall.modes;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.teb.kilimanjaro.AppPrefs;
import com.teb.kilimanjaro.R;
import com.teb.kilimanjaro.activitys.BaseActivity;
import com.teb.kilimanjaro.adapters.BetListtAdapter;
import com.teb.kilimanjaro.configs.HandlerConfig;
import com.teb.kilimanjaro.models.bean.ModeAutoList;
import com.teb.kilimanjaro.models.entry.hall.OddsListModel;
import com.teb.kilimanjaro.mvp.presenter.BettingPresenterImp;
import com.teb.kilimanjaro.mvp.view.BettingViewInf;
import com.teb.kilimanjaro.utils.MyUtil;
import com.teb.kilimanjaro.utils.StringUtil;
import com.teb.kilimanjaro.utils.ToastUtil;
import com.teb.kilimanjaro.views.dialogs.BetAllDialog;
import com.teb.kilimanjaro.views.MyActionBar;
import com.teb.kilimanjaro.views.dialogs.ModeAutoNameDialog;
import com.teb.kilimanjaro.views.dialogs.MyDialog;

import java.util.HashMap;
import java.util.List;

/**
 * Created by yangbin on 16/7/26.
 * 模式编辑
 */
public class ModeEditActivity extends BaseActivity implements
        BetListtAdapter.OnBetCoinItemClickListener, View.OnClickListener,
        MyUtil.OnKeyboardItemClickLisenter, MyUtil.OnTimesItemClickLisenter,
        MyUtil.OnOtherModeItemClickLisenter, BettingViewInf {

    private View mView;

    private MyActionBar mMyActionBar;

    private EditText mModeNameET;

    private ListView mListView;
    private List<OddsListModel.OddsData> mOddsDataList;
    private BetListtAdapter mBetListtAdapter;
    private LinearLayout mMyKeyboardLL;

    private int mItemIndex = 0;

    // Bottom
    private TextView mSaveOtherTV, mSaveTV;// 另存为    保存

    private TextView mTotalBetCountTV;
    private int mTotalBetCount;// 总注数
    private TextView mGameCoinTV;

    private boolean isNewMode;// 是否新建
    private ModeAutoList.ModeAutoData mModeAutoData;// 传递过来的对象

    private Handler mHandler = new Handler();
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            mListView.setSelection(mItemIndex);// 滚到可见状态
        }
    };

    private BettingPresenterImp mBettingPresenterImp;
    private Dialog mLoadingDialog;

    private long mGameCoin;
    
    private void setOddsDataList(List<OddsListModel.OddsData> oddsDataList) {
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mView = LayoutInflater.from(this).inflate(R.layout.activity_mode_edit, null);
        setContentView(mView);

        mGameCoin = AppPrefs.getInstance().getGameLong();

        // 接受传递来的对象
        isNewMode = getIntent().getBooleanExtra("new", false);
        mModeAutoData = (ModeAutoList.ModeAutoData) getIntent().getSerializableExtra("data");

        mBettingPresenterImp = new BettingPresenterImp(this);
        mLoadingDialog = MyDialog.createLoadingDialog(this, true);
        initViews();

        if (isNewMode) {
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("gameId", AppPrefs.getInstance().getSelectedGame().getId());
            mBettingPresenterImp.getOddsList(StringUtil.getJson(hashMap));
            mLoadingDialog.show();
        } else {
            setOddsDataList(mModeAutoData.getList());
        }
    }

    private void initViews() {
        // ******MyActionBar
        mMyActionBar = (MyActionBar) findViewById(R.id.actionBar);
        mMyActionBar.setBarTitleText(R.string.mode_edit_bar);
        mMyActionBar.setBarBackGround(R.color.main_background);
        mMyActionBar.setBarLeftImage(R.drawable.ic_back_btn);
        mMyActionBar.setOnActionBarClickListener(new MyActionBar.OnActionBarClickListener() {
            @Override
            public void onActionBarLeftClicked() {
                finish();
            }

            @Override
            public void onActionBarRightClicked() {

            }
        });

        mMyKeyboardLL = (LinearLayout) findViewById(R.id.ll_my_keyboard);

        mModeNameET = (EditText) findViewById(R.id.et_mode_name);
        if (mModeAutoData != null) mModeNameET.setText(mModeAutoData.getName());
        mModeNameET.setSelection(mModeNameET.getText().toString().trim().length());

        // ******投注界面ListView
        mListView = (ListView) findViewById(R.id.lv_mode_edit);
        mListView.addHeaderView(initHeaderView());

        mBetListtAdapter = new BetListtAdapter();
        mListView.setAdapter(mBetListtAdapter);
        mBetListtAdapter.setOnBetCoinItemClickListener(this);

        // ******bottom
        mSaveOtherTV = (TextView) findViewById(R.id.btn_save_other);
        mSaveOtherTV.setOnClickListener(this);
        mSaveTV = (TextView) findViewById(R.id.btn_save);
        mSaveTV.setOnClickListener(this);

        // ******键盘
        MyUtil.setOnKeyboardItemClickLisenter(mView, this);

        mTotalBetCountTV = (TextView) findViewById(R.id.tv_total_bet);
        mGameCoinTV = (TextView) findViewById(R.id.tv_amount);

        // 倍数按钮
        MyUtil.setOnTimesItemClickLisenter(mView, this);

        // 其他模式按钮
        MyUtil.setOnOtherModeItemClickLisenter(mView, this);
    }

    /**
     * 投注界面头部倍数选择
     *
     * @return
     */
    private View initHeaderView() {
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        AbsListView.LayoutParams params = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT);
        linearLayout.setLayoutParams(params);
        linearLayout.setBackgroundResource(R.color.gray_line);

        View view0 = LayoutInflater.from(this).inflate(R.layout.addview_bet_times_header, null);
        linearLayout.addView(view0);

        View view1 = LayoutInflater.from(this).inflate(R.layout.addview_bet_times_other_header, null);
        linearLayout.addView(view1, 1);
        return linearLayout;
    }

    /**
     * 计算总投注数量(内部使用)
     */
    private void handlerBetCountTV(List<OddsListModel.OddsData> tempList, boolean isNeedVerify) {
        if (isNeedVerify) {// 是否需要验证(一般只在做乘法计算时计算)
            // 判断计算结果
            long[] temp = MyUtil.getBetCountAndTotalCoin(tempList);
            if (temp[1] < 0 || temp[1] > mGameCoin) {// 小于0是指在计算结果过大的时候
                tempList = MyUtil.handlerOddsListByBetAll(tempList, mGameCoin, temp);
            }
        }
        mOddsDataList = tempList;

        double[] tempAmount = MyUtil.getBetInfo(mOddsDataList);
        mTotalBetCount = (int) tempAmount[0];
        mTotalBetCountTV.setText(mTotalBetCount + "");
        mGameCoinTV.setText(mGameCoin + "");
        mBetListtAdapter.setList(mOddsDataList);
    }

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

    @Override
    public void onTimesItemClickListener(int position, float times) {
        OddsListModel.OddsData data = mOddsDataList.get(position);
        if (data.isBeted()) return;// 已经投注过的，不允许修改
        data.setBetCoin((long) (data.getBetCoin() * times));
        data.setSelected(data.getBetCoin() > 0);
        mOddsDataList.set(position, data);
        handlerBetCountTV(mOddsDataList, false);
    }

    @Override
    public void onNumItemClickListener(int position) {
        OddsListModel.OddsData data = mOddsDataList.get(position);
        if (data.isBeted()) return;// 已经投注过的，不允许修改
        data.setBetCoin(!data.isSelected() ? data.getDefaultBet() : 0);
        data.setSelected(data.getBetCoin() > 0);
        mOddsDataList.set(position, data);
        handlerBetCountTV(mOddsDataList, false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_save:
                saveMode(mModeNameET.getText().toString().trim(), isNewMode);
                break;
            case R.id.btn_save_other:
                new ModeAutoNameDialog(this)
                        .setMessage(mModeNameET.getText().toString().trim())
                        .listener(new ModeAutoNameDialog.OnDialogClickListener() {
                            @Override
                            public void onRightClicked(String text) {
                                saveMode(text, true);
                            }
                        })
                        .show();
                break;
        }
    }

    /**
     * 保存模式
     */
    private void saveMode(String modeName, boolean isNew) {
        mModeNameET.setText(modeName);
        mModeNameET.setSelection(modeName.length());

        if (TextUtils.isEmpty(modeName)) {
            ToastUtil.showToast(R.string.mode_edit_input_modename);
            return;
        }
        if (mTotalBetCount == 0) {
            return;
        }

        List<ModeAutoList.ModeAutoData> list = AppPrefs.getInstance().getModeAutoList();
        if (isNew) {// 新建
            // 判断是否存在同名的模式
            ModeAutoList.ModeAutoData modeAutoData = MyUtil.findModeAutoByName(modeName);
            if (modeAutoData.getName() != null) {
                ToastUtil.showToast(modeName + getResources().getString(R.string.mode_edit_modename_had));
                return;
            }
            modeAutoData.setName(modeName);
            modeAutoData.setList(mOddsDataList);
            modeAutoData.setTotalCount(mTotalBetCount);
            list.add(modeAutoData);
        } else {// 修改
            for (int i = 0; i < list.size(); i++) {
                ModeAutoList.ModeAutoData data = list.get(i);
                if (mModeAutoData.getId() == data.getId()) {
                    data.setName(modeName);
                    data.setList(mOddsDataList);
                    data.setTotalCount(mTotalBetCount);
                    list.set(i, data);
                    break;
                }
            }
        }
        AppPrefs.getInstance().saveModeList(list);
        finish();
    }

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

    @Override
    public void onTimesItemClickLisenter(float times) {
        if (times == -1) {
            handlerBetCountTV(AppPrefs.getInstance().getLastBetList(), false);
        } else if (times == -2) {
            handlerBetCountTV(MyUtil.handlerOddsListByTurn(mOddsDataList), true);
        } else if (times == -3) {
            final long[] temp = MyUtil.getBetCountAndTotalCoin(mOddsDataList);
            if (temp[0] > 0) {// 有投注
                new BetAllDialog(this, 0)
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

    @Override
    public void onOtherModeItemClickLisenter(String[] modeNums) {
        List<OddsListModel.OddsData> tempList = MyUtil.handlerOddsList(modeNums, mOddsDataList);
        handlerBetCountTV(tempList, false);
    }

    @Override
    public void sendOddsMessage(Message msg) {
        if (mLoadingDialog != null) mLoadingDialog.dismiss();
        switch (msg.what) {
            case HandlerConfig.WHAT_POST_SUCCESS:
                setOddsDataList((List<OddsListModel.OddsData>) msg.obj);
                break;
            default:
                MyUtil.handMessage(this, msg, "ModeEditActivity---Odds>>>");
                break;
        }
    }

    @Override
    public void sendBetMessage(Message msg) {
        // 无用
    }
}
