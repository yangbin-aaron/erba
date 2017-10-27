package com.teb.kilimanjaro.views.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;

import com.teb.kilimanjaro.App;
import com.teb.kilimanjaro.AppPrefs;
import com.teb.kilimanjaro.R;
import com.teb.kilimanjaro.adapters.ModeAutoDialogAdapter;
import com.teb.kilimanjaro.models.bean.ModeAutoList;

import java.util.List;

/**
 * Created by yangbin on 16/7/26.
 */
public class ModeAutpListDialog extends Dialog implements ModeAutoDialogAdapter.OnModeAutoItemClickListener {

    private ListView mListView;
    private ModeAutoDialogAdapter mAdapter;
    private List<ModeAutoList.ModeAutoData> mList;

    public ModeAutpListDialog(Context context) {
        super(context, R.style.StylesDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_dialog_modelist_layout);
        setCancelable(true);
        initViews();

        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = App.getAppContext().getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        lp.width = d.widthPixels;
        dialogWindow.setAttributes(lp);
    }

    private void initViews() {
        View topView = findViewById(R.id.dialog_top_layout);
        topView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        mListView = (ListView) findViewById(R.id.lv_mode_list);
        mAdapter = new ModeAutoDialogAdapter();
        mListView.setAdapter(mAdapter);
        mList = AppPrefs.getInstance().getModeAutoList();
        mAdapter.setList(mList);
        mAdapter.setOnModeAutoItemClickListener(this);
    }

    @Override
    public void onModeAutoItemClickListener(int position) {
        AppPrefs.getInstance().saveModeStart(mList.get(position));
        mOnDialogClickListener.onSelectModetClicked();
        dismiss();
    }

    private OnDialogClickListener mOnDialogClickListener = null;

    public ModeAutpListDialog listener(OnDialogClickListener listener) {
        mOnDialogClickListener = listener;
        return this;
    }

    /**
     * 两个按钮使用
     */
    public interface OnDialogClickListener {
        void onSelectModetClicked();
    }
}
