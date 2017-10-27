package com.teb.kilimanjaro.activitys.hall.modes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.teb.kilimanjaro.AppPrefs;
import com.teb.kilimanjaro.R;
import com.teb.kilimanjaro.adapters.ModeAutoAdapter;
import com.teb.kilimanjaro.models.bean.ModeAutoList;
import com.teb.kilimanjaro.utils.MyUtil;
import com.teb.kilimanjaro.views.MyActionBar;
import com.teb.kilimanjaro.views.dialogs.ModeAutoNameDialog;

import java.util.List;

/**
 * Created by yangbin on 16/7/26.
 * 模式管理    不区分北京28和加拿大28
 */
public class ModeManageActivity extends Activity implements View.OnClickListener, ModeAutoAdapter.OnModeAutoItemClickListener {

    private MyActionBar mMyActionBar;

    private RelativeLayout mNewModeRL;
    private LinearLayout mNewModeBtnByNoMode, mNewModeBtnByHasMode;

    private List<ModeAutoList.ModeAutoData> mModeAutoDatas;
    private ListView mListView;
    private ModeAutoAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mode_manage);
        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mModeAutoDatas = AppPrefs.getInstance().getModeAutoList();
        if (mModeAutoDatas.size() > 0) {
            mAdapter.setList(mModeAutoDatas);
            mNewModeRL.setVisibility(View.GONE);
        } else {
            mNewModeRL.setVisibility(View.VISIBLE);
        }
    }

    private void initViews() {
        // ******MyActionBar
        mMyActionBar = (MyActionBar) findViewById(R.id.actionBar);
        mMyActionBar.setBarBackGround(R.color.main_background);
        mMyActionBar.setBarTitleText(R.string.mode_manage_bar);
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

        // ******没有数据时的布局
        mNewModeRL = (RelativeLayout) findViewById(R.id.rl_new_mode_nohasmode);
        mNewModeBtnByNoMode = (LinearLayout) findViewById(R.id.ll_new_mode_nohasmode);
        mNewModeBtnByHasMode = (LinearLayout) findViewById(R.id.ll_new_mode_hasmode);
        mNewModeBtnByNoMode.setOnClickListener(this);
        mNewModeBtnByHasMode.setOnClickListener(this);

        // ******ListView
        mListView = (ListView) findViewById(R.id.lv_mode);
        mAdapter = new ModeAutoAdapter();
        mAdapter.setOnModeAutoItemClickListener(this);
        mListView.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_new_mode_hasmode:// 新建模式
            case R.id.ll_new_mode_nohasmode:// 新建模式
                intoModeEditActivity(true, null);
                break;
        }
    }

    private void intoModeEditActivity(boolean isNew, ModeAutoList.ModeAutoData data) {
        Intent intent = new Intent(this, ModeEditActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean("new", isNew);// 新建   还是   修改
        bundle.putSerializable("data", data);
        intent.putExtras(bundle);
        MyUtil.startActivity(this,intent);
    }

    @Override
    public void onModeAutoItemDeleteClickListener(int position) {
        mModeAutoDatas.remove(position);
        AppPrefs.getInstance().saveModeList(mModeAutoDatas);
        mAdapter.setList(mModeAutoDatas);
    }

    @Override
    public void onModeAutoItemUpdateNameClickListener(final int position) {
        ModeAutoList.ModeAutoData data = mModeAutoDatas.get(position);
        new ModeAutoNameDialog(this)
                .setMessage(data.getName())
                .listener(new ModeAutoNameDialog.OnDialogClickListener() {
                    @Override
                    public void onRightClicked(String text) {
                        updateModeName(position, text);
                    }
                })
                .show();
    }

    private void updateModeName(int position, String name) {
        ModeAutoList.ModeAutoData data = mModeAutoDatas.get(position);
        data.setName(name);
        mModeAutoDatas.set(position,data);
        AppPrefs.getInstance().saveModeList(mModeAutoDatas);
        mAdapter.setList(mModeAutoDatas);
    }

    @Override
    public void onModeAutoItemClickListener(int position) {
        intoModeEditActivity(false, mModeAutoDatas.get(position));
    }
}
