package com.teb.kilimanjaro.activitys;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.teb.kilimanjaro.App;
import com.teb.kilimanjaro.AppPrefs;
import com.teb.kilimanjaro.R;
import com.teb.kilimanjaro.adapters.DrawerGameListAdapter;
import com.teb.kilimanjaro.configs.BroadcastConfig;
import com.teb.kilimanjaro.configs.HandlerConfig;
import com.teb.kilimanjaro.fragments.HallFragment;
import com.teb.kilimanjaro.fragments.MineFragment;
import com.teb.kilimanjaro.fragments.TrendFragment;
import com.teb.kilimanjaro.models.entry.hall.GameListModel;
import com.teb.kilimanjaro.models.entry.mine.LoginModel;
import com.teb.kilimanjaro.models.entry.mine.VersionStatusModel;
import com.teb.kilimanjaro.mvp.presenter.MainPresenterImp;
import com.teb.kilimanjaro.mvp.view.MainViewInf;
import com.teb.kilimanjaro.utils.MyUtil;
import com.teb.kilimanjaro.utils.StringUtil;
import com.teb.kilimanjaro.utils.ToastUtil;
import com.teb.kilimanjaro.views.CircleImageView;
import com.teb.kilimanjaro.views.dialogs.DownApkDialog;
import com.teb.kilimanjaro.views.dialogs.MyDialog;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by yangbin on 16/7/1
 * 程序主Activity
 */
public class MainActivity extends FragmentActivity implements OnClickListener, MainViewInf {

    private MainPresenterImp mMainPresenterImp;

    private DrawerLayout mDrawerLayout;// 抽屉
    private boolean mDrawerIsOpen = false;
    private CircleImageView mPhotoIV;// 头像
    private TextView mNickNameTV, mUidTV;
    private ListView mGameListView;//场次列表

    private List<GameListModel.GameListData> mGameList;
    private DrawerGameListAdapter mAdapter;

    private long mExitTime;// 点击返回键的时间

    private TextView[] mBottomTabs;// BottomBar集合:0大厅按钮索引,1趋势按钮索引,2我的按钮索引
    private int mCurrentTabIndex = 0;// 当前选择的按钮索引

    private List<Fragment> mFragmentList;
    private FrameLayout[] mFrameLayouts;

    private Dialog mLoadingDialog;// 加载对话框

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(BroadcastConfig.ACTION_EXIT_APP)) {
                finish();
            } else if (action.equals(BroadcastConfig.ACTION_UPDATE_GAME)) {// 刷新所有数据
                if (AppPrefs.getInstance().getGameListJson() == null) {
                    getGameList();
                    return;
                }
                mLoadingDialog.show();
                ((HallFragment) mFragmentList.get(0)).initData();
                ((TrendFragment) mFragmentList.get(1)).initData();
                closeDrawerLayout();
            } else if (action.equals(BroadcastConfig.ACTION_REFRESH_HALLDATA)) {// 刷新大厅数据
                boolean isSHowDialog = intent.getBooleanExtra("show_dialog", true);
                ((HallFragment) mFragmentList.get(0)).onRefresh(isSHowDialog);
            } else if (action.equals(BroadcastConfig.ACTION_UPDATE_NEW_GAMECOIN)) {// 刷新最新的游戏币金额
                ((HallFragment) mFragmentList.get(0)).getGameCoin(false);
            } else if (action.equals(BroadcastConfig.ACTION_LOGOUT)) {// 退出用户登录
                ((MineFragment) mFragmentList.get(2)).logouted();
            }
        }
    };

    /**
     * 统一设置积分
     */
    public void setFragmentGameCoin() {
        ((HallFragment) mFragmentList.get(0)).setGameCoin();
        ((MineFragment) mFragmentList.get(2)).setGameCoin();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);// 强制为竖屏
        setContentView(R.layout.activity_main);
        mLoadingDialog = MyDialog.createLoadingDialog(this, true);
        mMainPresenterImp = new MainPresenterImp(this);
        initViews();

        getUpdateAppInfo();

        // 注册广播 
        IntentFilter filter = new IntentFilter();
        filter.addAction(BroadcastConfig.ACTION_UPDATE_GAME);
        filter.addAction(BroadcastConfig.ACTION_REFRESH_HALLDATA);
        filter.addAction(BroadcastConfig.ACTION_UPDATE_NEW_GAMECOIN);
        filter.addAction(BroadcastConfig.ACTION_LOGOUT);
        registerReceiver(mReceiver, filter);
    }

    public void getUpdateAppInfo() {
        // 检测版本
        mMainPresenterImp.getUpdateApp(StringUtil.getVersionJson());
        mLoadingDialog.show();
    }

    private void getGameList() {
        getGameList(true);
    }

    private void getGameList(boolean isShowDialog) {
        mMainPresenterImp.getGameList(StringUtil.getNullJson());
        if (isShowDialog) mLoadingDialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("MainActivity"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("MainActivity"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }

    /**
     * 初始化控件
     */
    private void initViews() {
        // ******初始化抽屉
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        mPhotoIV = (CircleImageView) findViewById(R.id.user_photo_iv);
        mNickNameTV = (TextView) findViewById(R.id.tv_nickname);
        mUidTV = (TextView) findViewById(R.id.tv_uid);
        LoginModel model = LoginModel.getUserInfo();
        if (model != null) {
            mNickNameTV.setText(model.getData().getNickName());
            mUidTV.setText("UID：" + model.getData().getUserName() + "");
            String uri = model.getData().getAvatar();
            ImageAware imageAware = new ImageViewAware(mPhotoIV, false);
            mPhotoIV.setTag(uri);
            ImageLoader.getInstance().displayImage(uri, imageAware);
        }

        mGameListView = (ListView) findViewById(R.id.drawer_listview);
        mDrawerLayout.setDrawerListener(mSimpleDrawerListener);

        // ******初始化BottomBar
        mBottomTabs = new TextView[3];// 初始化3个按钮
        int[] tabIds = {R.id.tv_hall, R.id.tv_trend, R.id.tv_mine};
        for (int i = 0; i < tabIds.length; i++) {
            mBottomTabs[i] = (TextView) findViewById(tabIds[i]);
            mBottomTabs[i].setOnClickListener(this);
        }
        // 默认第一项被选中
        mBottomTabs[mCurrentTabIndex].setSelected(true);

        // ******初始化ViewPager(已经被替换)
        mFragmentList = new ArrayList<>();
        mFragmentList.add(new HallFragment());
        mFragmentList.add(new TrendFragment());
        mFragmentList.add(new MineFragment());

        initFragment();
    }

    private void initFragment() {
        mFrameLayouts = new FrameLayout[3];
        mFrameLayouts[0] = (FrameLayout) findViewById(R.id.kl_framelayout0);
        mFrameLayouts[1] = (FrameLayout) findViewById(R.id.kl_framelayout1);
        mFrameLayouts[2] = (FrameLayout) findViewById(R.id.kl_framelayout2);

        // 添加第一个界面
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction f_transaction = fragmentManager.beginTransaction();
        f_transaction.replace(R.id.kl_framelayout0, mFragmentList.get(0));
        f_transaction.replace(R.id.kl_framelayout1, mFragmentList.get(1));
        f_transaction.replace(R.id.kl_framelayout2, mFragmentList.get(2));
        f_transaction.commit();
    }

    // 抽屉打开时的监听
    private DrawerLayout.SimpleDrawerListener mSimpleDrawerListener = new DrawerLayout.SimpleDrawerListener() {
        @Override
        public void onDrawerOpened(View drawerView) {
            super.onDrawerOpened(drawerView);
            mDrawerIsOpen = true;
            Log.e("aaron", "onDrawerOpened------");
            setGameListView();
        }

        @Override
        public void onDrawerClosed(View drawerView) {
            super.onDrawerClosed(drawerView);
            mDrawerIsOpen = false;
        }
    };

    /**
     * 设置GameListView数据
     */

    private void setGameListView() {
        if (mGameList != null) return;
        String json = AppPrefs.getInstance().getGameListJson();
        if (json != null) {
            mGameList = new Gson().fromJson(json, GameListModel.class).getData();
            mAdapter = new DrawerGameListAdapter(mGameList);
            mGameListView.setAdapter(mAdapter);
        }
    }

    /**
     * 点击事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_hall:// 大厅
                updateBottomBarState(0);
                break;
            case R.id.tv_trend:// 趋势
                updateBottomBarState(1);
                break;
            case R.id.tv_mine:// 我的
                updateBottomBarState(2);
                break;
        }
    }

    /**
     * 改变BottomBar选中状态
     *
     * @param index 选择的按钮索引
     */
    private void updateBottomBarState(int index) {
        if (index == mCurrentTabIndex) return;
        // 将点击前的按钮设为未选中状态
        mBottomTabs[mCurrentTabIndex].setSelected(false);
        // 将当前点击的按钮设为选中状态
        mCurrentTabIndex = index;
        mBottomTabs[mCurrentTabIndex].setSelected(true);
        // 限制不能拉开抽屉
        if (mCurrentTabIndex == 0) {
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        } else {
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }
        // 切换页面
        for (int i = 0; i < mFrameLayouts.length; i++) {
            if (i == mCurrentTabIndex) {
                mFrameLayouts[i].setVisibility(View.VISIBLE);
            } else {
                mFrameLayouts[i].setVisibility(View.GONE);
            }
        }
    }

    /**
     * 返回按钮监控
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mDrawerIsOpen) {
                mDrawerLayout.closeDrawer(Gravity.LEFT);
                return true;
            }
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                ToastUtil.showToast(R.string.app_back_hint);
                mExitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 打开抽屉
     */
    public void openDrawerLayout() {
        mDrawerLayout.openDrawer(Gravity.LEFT);
    }

    /**
     * 关闭抽屉
     */
    public void closeDrawerLayout() {
        mDrawerLayout.closeDrawer(Gravity.LEFT);
    }


    private int comLoadCount = 0;// 完成的访问网络次数

    public void showLoadingDialog(String tag) {
        comLoadCount++;
        Log.e("tag", tag + ">>>>" + comLoadCount);
//        if (comLoadCount == 1) {
//            mLoadingDialog.show();
//        }
    }

    public void dismissLoadingDialog(String tag) {
        comLoadCount--;
        Log.e("tag", tag + ">>>>" + comLoadCount);
        if (comLoadCount == 0) {
            if (mLoadingDialog != null)
                mLoadingDialog.dismiss();
        }
    }

    // --------------------MainViewInf method---------------------

    @Override
    public void sendVersionMessage(final Message msg) {
        switch (msg.what) {
            case HandlerConfig.WHAT_POST_SUCCESS:
                // 如果成功则继续获取其他信息(判断是否需要更新，再执行下面的操作)
                final VersionStatusModel.VersionStatusData data = (VersionStatusModel.VersionStatusData) msg.obj;
                switch (data.getVersionStatus()) {
                    case 0:
                        getGameList(false);
                        break;
                    case -1:
                    case 1:
                        AppPrefs.getInstance().saveSelectedGame(-1);
                        AppPrefs.getInstance().saveGameListJson(null);
                        final App app = (App) getApplication();
                        app.haveNewVersion = true;// 有新版本
                        if (mLoadingDialog != null) mLoadingDialog.dismiss();
                        MyDialog.createUpdateAppDialog(this, data.getVersionStatus() == -1)
                                .listener(new MyDialog.OnDialogClickListener() {
                                    @Override
                                    public void onLeftClicked() {
                                        if (data.getVersionStatus() == 1) {
                                            getGameList(true);
                                        }
                                    }

                                    @Override
                                    public void onRightClicked() {
//                                        Uri uri = Uri.parse(data.getUrl());
//                                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                                        startActivity(intent);
//                                        finish();
                                        MyUtil.downLoadApk(data.getUrl());
                                        new DownApkDialog(MainActivity.this).show();
                                    }
                                }).show();
                        break;
                }
                break;
            default:
                if (mLoadingDialog != null) mLoadingDialog.dismiss();
                AppPrefs.getInstance().saveSelectedGame(-1);
                AppPrefs.getInstance().saveGameListJson(null);
                MyUtil.handMessage(this, msg, "MainActivity---Version>>>");
        }
    }

    @Override
    public void sendGameListMessage(Message msg) {
        switch (msg.what) {
            case HandlerConfig.WHAT_POST_SUCCESS:
                setGameListView();
                sendBroadcast(new Intent(BroadcastConfig.ACTION_UPDATE_GAME));
                break;
            default:
                if (mLoadingDialog != null)
                    mLoadingDialog.dismiss();
                AppPrefs.getInstance().saveSelectedGame(-1);
                AppPrefs.getInstance().saveGameListJson(null);
                MyUtil.handMessage(this, msg, "MainActivity---GameList>>>");
                break;
        }
    }
}
