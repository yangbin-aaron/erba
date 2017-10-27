package com.teb.kilimanjaro.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.teb.kilimanjaro.AppPrefs;
import com.teb.kilimanjaro.R;
import com.teb.kilimanjaro.activitys.coin.BillActivity;
import com.teb.kilimanjaro.activitys.coin.RechargeActivity;
import com.teb.kilimanjaro.activitys.coin.TieOnCardActivity;
import com.teb.kilimanjaro.activitys.coin.WithdrawActivity;
import com.teb.kilimanjaro.activitys.mine.AboutActivity;
import com.teb.kilimanjaro.activitys.mine.LoginActivity;
import com.teb.kilimanjaro.activitys.mine.UpdatePwdActivity;
import com.teb.kilimanjaro.adapters.MineAdapter;
import com.teb.kilimanjaro.configs.HandlerConfig;
import com.teb.kilimanjaro.models.entry.coin.PrepareInfoModel;
import com.teb.kilimanjaro.models.entry.mine.LoginModel;
import com.teb.kilimanjaro.mvp.presenter.MinePresenterImp;
import com.teb.kilimanjaro.mvp.view.MineViewInf;
import com.teb.kilimanjaro.utils.MyUtil;
import com.teb.kilimanjaro.utils.NetUtil;
import com.teb.kilimanjaro.utils.StringUtil;
import com.teb.kilimanjaro.views.CircleImageView;
import com.teb.kilimanjaro.views.MyActionBar;
import com.teb.kilimanjaro.views.dialogs.MyDialog;
import com.teb.kilimanjaro.views.dialogs.PwdDialog;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;

/**
 * Created by yangbin on 16/7/1.
 * 我的 界面fragment
 */
public class MineFragment extends Fragment implements View.OnClickListener, MineViewInf {

    private MyActionBar mMyActionBar;// ActionBar

    // 整个布局的listview（自定义）
    private PullToRefreshListView mListView;
    private View mFooterView;
    // listView适配器（里面含有点击事件）
    private MineAdapter mMineAdapter;

    // 昵称
    private TextView mNickNameTV;
    private CircleImageView mHeaderPhotoIV;// 头像
    private LinearLayout mGotoBillLL;// 去账单明细
    private LinearLayout mGotoTYLL;// 去体验币
    private TextView mGameCoinTV;// 金豆
    private TextView mGameScoreTV;// 游戏积分

    // mvp 我的Presenter层
    private MinePresenterImp mMinePresenterImp;
    private Dialog mLoadingDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 实例化presenter层
        mMinePresenterImp = new MinePresenterImp(this);
        mLoadingDialog = MyDialog.createLoadingDialog(getActivity(), false);
    }

    private View rootView = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_mine, null);
        }
        //缓存的rootView需要判断是否已经被加过parent， 如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViews();

        setUserInfo();
    }

    /**
     * 设置用户相关数据
     */
    private void setUserInfo() {
        LoginModel model = LoginModel.getUserInfo();
        if (model != null) {
            mNickNameTV.setText(model.getData().getNickName());
            String uri = model.getData().getAvatar();
            ImageAware imageAware = new ImageViewAware(mHeaderPhotoIV, false);
            mHeaderPhotoIV.setTag(uri);
            ImageLoader.getInstance().displayImage(uri, imageAware);
            setGameCoin();
        }
    }

    /**
     * 初始化组件
     */
    private void initViews() {
        // ******MyActionBar
        mMyActionBar = (MyActionBar) getView().findViewById(R.id.actionBar);
        mMyActionBar.setBarBackGround(R.color.main_background);
        mMyActionBar.setBarTitleText(R.string.bottom_tab_mine);

        initUserInfoView();

        // ******ListView
        mListView = (PullToRefreshListView) getView().findViewById(R.id.mine_listview);
        mListView.setMode(PullToRefreshBase.Mode.DISABLED);
        initFooter();
        mListView.getRefreshableView().addFooterView(mFooterView);
        // 初始化适配器
        mMineAdapter = new MineAdapter();
        mListView.setAdapter(mMineAdapter);
        // 设置刷新事件监听
//        mListView.setOnRefreshListener(mListener);
    }

    /**
     * 初始化用户信息部分
     *
     * @return
     */
    private void initUserInfoView() {
        mNickNameTV = (TextView) getView().findViewById(R.id.tv_nickname);
        TextView logoutBtn = (TextView) getView().findViewById(R.id.btn_logout);
        logoutBtn.setOnClickListener(this);
        mHeaderPhotoIV = (CircleImageView) getView().findViewById(R.id.user_photo_iv);
        mGotoBillLL = (LinearLayout) getView().findViewById(R.id.ll_goto_bill);
        mGotoBillLL.setOnClickListener(this);
        mGotoTYLL = (LinearLayout) getView().findViewById(R.id.ll_tiyanbi);
        mGotoTYLL.setOnClickListener(this);
        mGameCoinTV = (TextView) getView().findViewById(R.id.tv_gamecoin);
        mGameScoreTV = (TextView) getView().findViewById(R.id.tv_score);
    }

    /**
     * 设置余额
     */
    public void setGameCoin() {
        mGameCoinTV.setText(AppPrefs.getInstance().getGameCoinLong() + "");
        mGameScoreTV.setText(AppPrefs.getInstance().getGameScoreLong() + "");
    }

    /**
     * 初始化其他按钮
     *
     * @return
     */
    private void initFooter() {
        mFooterView = LayoutInflater.from(getActivity()).inflate(R.layout.addview_mine_footer, null);
        RelativeLayout addGameCoinRL = (RelativeLayout) mFooterView.findViewById(R.id.rl_add_game_coin);
        RelativeLayout withdrawRL = (RelativeLayout) mFooterView.findViewById(R.id.rl_withdraw);
        RelativeLayout noticeRL = (RelativeLayout) mFooterView.findViewById(R.id.rl_notice);
        RelativeLayout modefypwdRL = (RelativeLayout) mFooterView.findViewById(R.id.rl_modify_pwd);
        RelativeLayout aboutRL = (RelativeLayout) mFooterView.findViewById(R.id.rl_about);
        addGameCoinRL.setOnClickListener(this);
        withdrawRL.setOnClickListener(this);
        noticeRL.setOnClickListener(this);
        modefypwdRL.setOnClickListener(this);
        aboutRL.setOnClickListener(this);
    }

    /**
     * 下拉刷新
     */
//    private PullToRefreshBase.OnRefreshListener2<ListView> mListener = new PullToRefreshBase.OnRefreshListener2<ListView>() {
//        @Override
//        public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
//            mListView.onRefreshComplete();
//            // 获取用户信息（包含余额相关）
//        }
//
//        @Override
//        public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
//            ToastUtil.showToast("111111111111");
//        }
//    };
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_goto_bill:
                MyUtil.startActivity(getActivity(), new Intent(getActivity(), BillActivity.class));
                break;
            case R.id.ll_tiyanbi:
//                MyDialog.showWaitDialog(getActivity());
                break;
            // --------footer--------
            case R.id.rl_add_game_coin:// 充值
                MyUtil.startActivity(getActivity(), new Intent(getActivity(), RechargeActivity.class));
//                MyUtil.startActivity(getActivity(), new Intent(getActivity(), HFBRechargeActivity.class));
                break;
            case R.id.rl_withdraw:// 提现
                new PwdDialog(getActivity())
                        .listener(new PwdDialog.OnDialogClickListener() {
                            @Override
                            public void onRightClicked(String pwd) {
                                HashMap<String, Object> hashMap = new HashMap<>();
                                hashMap.put("payPassword", NetUtil.MD5(pwd));
                                mMinePresenterImp.verifyPayPwd(StringUtil.getJson(hashMap));
                                mLoadingDialog.show();
                            }
                        })
                        .show();
                break;
            case R.id.rl_notice:// 消息通知

                break;
            case R.id.rl_modify_pwd:// 密码修改
                MyUtil.startActivity(getActivity(), new Intent(getActivity(), UpdatePwdActivity.class));
                break;
            case R.id.rl_about:// 关于我们
                MyUtil.startActivity(getActivity(), new Intent(getActivity(), AboutActivity.class));
                break;

            case R.id.btn_logout:// 退出
                // 是否需要弹出对话框提示：是否退出？ book
                new MyDialog(getActivity())
                        .setMessage(getResources().getString(R.string.hint_logout_str))
                        .setDefaultBtnText()
                        .listener(new MyDialog.OnDialogClickListener() {
                            @Override
                            public void onLeftClicked() {
                            }

                            @Override
                            public void onRightClicked() {
                                mMinePresenterImp.logout();
                            }
                        })
                        .show();
                break;
        }
    }

    // --------------------MineViewInf method---------------------

    @Override
    public void sendVerifyPayPwdMessage(Message msg) {
        switch (msg.what) {
            case HandlerConfig.WHAT_POST_SUCCESS:
                mMinePresenterImp.getPrepareInfo(StringUtil.getNullJson());
                break;
            default:
                if (mLoadingDialog != null) mLoadingDialog.dismiss();
                MyUtil.handMessage(getActivity(), msg, "MineFragment---VerifyPayPwd>>>");
        }
    }

    @Override
    public void sendMyCardMessage(Message msg) {
        if (mLoadingDialog != null) mLoadingDialog.dismiss();
        switch (msg.what) {
            case HandlerConfig.WHAT_POST_SUCCESS:
                PrepareInfoModel.PrepareInfoData infoData = (PrepareInfoModel.PrepareInfoData) msg.obj;
                Intent withdrawIntent = new Intent(getActivity(), WithdrawActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", infoData);
                withdrawIntent.putExtras(bundle);
                MyUtil.startActivity(getActivity(), withdrawIntent);
                break;
            case 10001:// 没有绑定银行卡
                MyUtil.startActivity(getActivity(), new Intent(getActivity(), TieOnCardActivity.class));
                break;
            default:
                MyUtil.handMessage(getActivity(), msg, "MineFragment---MyCard>>>");
                break;
        }
    }

    /**
     * 退出登录按钮的回调事件
     */
    @Override
    public void logouted() {
        MobclickAgent.onProfileSignOff();// 统计用户,账号登出时需调用此接口，调用之后不再发送账号相关内容
        // 进入登录Activity
        MyUtil.startActivity(getActivity(), new Intent(getActivity(), LoginActivity.class));
        // 销毁Activity
        getActivity().finish();
    }
}
