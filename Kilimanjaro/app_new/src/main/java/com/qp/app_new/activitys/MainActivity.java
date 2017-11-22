package com.qp.app_new.activitys;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.qp.app_new.App;
import com.qp.app_new.AppPrefs;
import com.qp.app_new.R;
import com.qp.app_new.adapters.MainViewPagerAdapter;
import com.qp.app_new.configs.NetStatusConfig;
import com.qp.app_new.contents.AppPrefsContent;
import com.qp.app_new.dialogs.DialogHelp;
import com.qp.app_new.fragments.HomeFragment;
import com.qp.app_new.fragments.MineFragment;
import com.qp.app_new.fragments.RankingFragment;
import com.qp.app_new.httpnetworks.NetWorkManager;
import com.qp.app_new.interfaces.NetListener;
import com.qp.app_new.interfaces.NormalDialogListener1;
import com.qp.app_new.utils.AppUtils;
import com.qp.app_new.utils.LogUtil;
import com.qp.app_new.utils.ToastUtil;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aaron on 2017/11/7.
 */

public class MainActivity extends FragmentActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {
    private static final int layoutId = R.layout.activity_main;

    private ViewPager mViewPager;
    private MainViewPagerAdapter mMainViewPagerAdapter;

    private HomeFragment mHomeFragment;
    private RankingFragment mRankingFragment;
    private MineFragment mMineFragment;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (layoutId);
        getUpdateAppInfo ();
        initViewPager ();
        initBottomTabs ();
    }

    private void initViewPager () {
        List<Fragment> fragmentList = new ArrayList<> ();
        mHomeFragment = new HomeFragment ();
        mRankingFragment = new RankingFragment ();
        mMineFragment = new MineFragment ();
        fragmentList.add (mHomeFragment);
        fragmentList.add (mRankingFragment);
        fragmentList.add (mMineFragment);
        mMainViewPagerAdapter = new MainViewPagerAdapter (getSupportFragmentManager (), fragmentList);
        mViewPager = (ViewPager) findViewById (R.id.viewPager);
        mViewPager.setAdapter (mMainViewPagerAdapter);
        mViewPager.setCurrentItem (0);
        mViewPager.setOffscreenPageLimit (3);
        mViewPager.setOnPageChangeListener (this);
    }

    private TextView[] mBottomTabs;// BottomBar集合:0大厅按钮索引,1趋势按钮索引,2我的按钮索引
    private int mCurrentTabIndex = 0;// 当前选择的按钮索引

    private void initBottomTabs () {
        mBottomTabs = new TextView[3];// 初始化3个按钮
        int[] tabIds = {R.id.tv_hall, R.id.tv_trend, R.id.tv_mine};
        for (int i = 0; i < tabIds.length; i++) {
            mBottomTabs[i] = (TextView) findViewById (tabIds[i]);
            mBottomTabs[i].setOnClickListener (this);
        }
        // 默认第一项被选中
        mBottomTabs[mCurrentTabIndex].setSelected (true);
    }

    @Override
    public void onPageScrolled (int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected (int position) {
        updateBottomBarState (position);
    }

    @Override
    public void onPageScrollStateChanged (int state) {
    }

    @Override
    public void onClick (View v) {
        switch (v.getId ()) {
            case R.id.tv_hall:
                updateBottomBarState (0);
                break;
            case R.id.tv_trend:
                updateBottomBarState (1);
                break;
            case R.id.tv_mine:// 我的
                updateBottomBarState (2);
                break;
        }
        mViewPager.setCurrentItem (mCurrentTabIndex);
    }

    /**
     * 改变BottomBar选中状态
     *
     * @param index 选择的按钮索引
     */
    private void updateBottomBarState (int index) {
        if (index == mCurrentTabIndex) return;
        // 将点击前的按钮设为未选中状态
        mBottomTabs[mCurrentTabIndex].setSelected (false);
        // 将当前点击的按钮设为选中状态
        mCurrentTabIndex = index;
        mBottomTabs[mCurrentTabIndex].setSelected (true);
    }

    private long mExitTime;// 点击返回键的时间

    /**
     * 返回按钮监控
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown (int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis () - mExitTime) > 2000) {
                ToastUtil.showToast (R.string.app_back_hint);
                mExitTime = System.currentTimeMillis ();
            } else {
                finish ();
            }
            return true;
        }
        return super.onKeyDown (keyCode, event);
    }

    /**
     * 登录  退出时调用此方法
     */
    public void updateData () {
        mHomeFragment.updateData ();
        mRankingFragment.updateData ();
        mMineFragment.updateData ();
    }

    /**
     * 检测版本
     */
    private void getUpdateAppInfo () {
        NetWorkManager.getInstance ().updateApp (null, new NetListener () {
            @Override
            public void onSuccessResponse (String msg, JSONObject jsonObject) {
                super.onSuccessResponse (msg, jsonObject);
                updateVersion (jsonObject);
            }

            @Override
            public void onErrorResponse (int errorWhat, String message) {
            }
        });
    }

    private void updateVersion (final JSONObject jsonObject) {
        // 如果成功则继续获取其他信息(判断是否需要更新，再执行下面的操作)
        int status = jsonObject.optInt ("versionStatus", 0);
        final App app = (App) getApplication ();
        app.haveNewVersion = true;// 有新版本
        switch (status) {
            case 0:
                app.haveNewVersion = false;
                break;
            case -1:// 强制更新
                DialogHelp.createMustDialog (this, getString (R.string.app_updateapp_must), new NormalDialogListener1 () {
                    @Override
                    public void onOkClickListener () {
                        AppUtils.startDownApk (MainActivity.this, jsonObject.optString ("url"));
                    }
                }).show ();
                break;
            case 1:
                DialogHelp.createOkDialog (this, getString (R.string.app_updateapp_), new NormalDialogListener1 () {
                    @Override
                    public void onOkClickListener () {
                        // 开始下载更新
                        AppUtils.startDownApk (MainActivity.this, jsonObject.optString ("url"));
                    }
                }).show ();
                break;
        }
    }

    @Override
    protected void onResume () {
        super.onResume ();
        MobclickAgent.onResume (this);
        if (AppPrefsContent.isLogined ()) {
            NetWorkManager.getInstance ().getGameCoin (new NetListener (this, false) {
                @Override
                public void onSuccessResponse (String msg, JSONObject jsonObject) {
                    super.onSuccessResponse (msg, jsonObject);
                    AppPrefs.getInstance ().saveGameCoin (jsonObject.optLong ("coin"));
                    mMineFragment.setGameCoin ();
                    LogUtil.e ("aaa", "aaa111");
                }

                @Override
                public void onErrorResponse (int errorWhat, String message) {
                    if (errorWhat == NetStatusConfig.STATUS_TOKEN_IS_UPDATED) {
                        LogUtil.e ("aaa", "aaa");
                        super.onErrorResponse (errorWhat, message);
                    }
                }
            });
        }
    }

    @Override
    protected void onPause () {
        super.onPause ();
        MobclickAgent.onPause (this);
    }
}
