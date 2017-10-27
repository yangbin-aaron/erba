package com.teb.kilimanjaro.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.teb.kilimanjaro.App;
import com.teb.kilimanjaro.AppPrefs;
import com.teb.kilimanjaro.R;
import com.teb.kilimanjaro.models.bean.ModeAutoList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangbin on 16/7/26.
 */
public class ModeAutoDialogAdapter extends BaseAdapter {

    private List<ModeAutoList.ModeAutoData> mList;
    private LayoutInflater mInflater;

    public ModeAutoDialogAdapter() {
        mList = new ArrayList<>();
        mInflater = (LayoutInflater) App.getAppContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setList(List<ModeAutoList.ModeAutoData> list) {
        mList = (list == null ? new ArrayList<ModeAutoList.ModeAutoData>() : list);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public ModeAutoList.ModeAutoData getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.adapter_mode_list_dialog, null);
            holder = new ViewHolder();
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_adapter_modename);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ModeAutoList.ModeAutoData data = getItem(position);
        holder.tv_name.setText(data.getName());
        String selectName = AppPrefs.getInstance().getModeStartName();
        if (selectName.equals(data.getName())) {
            holder.tv_name.setTextColor(App.getAppContext().getResources().getColor(R.color.white));
            holder.tv_name.setBackgroundResource(R.drawable.radio_blue_bg);
        } else {
            holder.tv_name.setTextColor(App.getAppContext().getResources().getColor(R.color.black_text));
            holder.tv_name.setBackgroundResource(R.drawable.radio_white_bg);
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnModeAutoItemClickListener.onModeAutoItemClickListener(position);
            }
        });

        return convertView;
    }

    class ViewHolder {
        TextView tv_name;
    }

    private OnModeAutoItemClickListener mOnModeAutoItemClickListener;

    public void setOnModeAutoItemClickListener(OnModeAutoItemClickListener onModeAutoItemClickListener) {
        mOnModeAutoItemClickListener = onModeAutoItemClickListener;
    }

    public interface OnModeAutoItemClickListener {
        void onModeAutoItemClickListener(int position);
    }
}
