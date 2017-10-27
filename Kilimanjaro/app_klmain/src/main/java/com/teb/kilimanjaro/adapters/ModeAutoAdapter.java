package com.teb.kilimanjaro.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.teb.kilimanjaro.App;
import com.teb.kilimanjaro.R;
import com.teb.kilimanjaro.models.bean.ModeAutoList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangbin on 16/7/26.
 */
public class ModeAutoAdapter extends BaseAdapter {

    private List<ModeAutoList.ModeAutoData> mList;
    private LayoutInflater mInflater;

    public ModeAutoAdapter() {
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
            convertView = mInflater.inflate(R.layout.adapter_mode_auto, null);
            holder = new ViewHolder();
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_update_name = (TextView) convertView.findViewById(R.id.tv_update_name);
            holder.tv_delete = (TextView) convertView.findViewById(R.id.tv_delete);
            holder.tv_total_count = (TextView) convertView.findViewById(R.id.tv_total_count);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ModeAutoList.ModeAutoData data = getItem(position);
        holder.tv_name.setText(data.getName());
        holder.tv_total_count.setText(data.getTotalCount() + "");

        holder.tv_update_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnModeAutoItemClickListener.onModeAutoItemUpdateNameClickListener(position);
            }
        });
        holder.tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnModeAutoItemClickListener.onModeAutoItemDeleteClickListener(position);
            }
        });
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnModeAutoItemClickListener.onModeAutoItemClickListener(position);
            }
        });

        return convertView;
    }

    class ViewHolder {
        TextView tv_name, tv_update_name, tv_delete, tv_total_count;
    }

    private OnModeAutoItemClickListener mOnModeAutoItemClickListener;

    public void setOnModeAutoItemClickListener(OnModeAutoItemClickListener onModeAutoItemClickListener) {
        mOnModeAutoItemClickListener = onModeAutoItemClickListener;
    }

    public interface OnModeAutoItemClickListener {
        void onModeAutoItemDeleteClickListener(int position);

        void onModeAutoItemUpdateNameClickListener(int position);

        void onModeAutoItemClickListener(int position);
    }
}
